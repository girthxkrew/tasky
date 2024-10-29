package com.rkm.tasky.network.interceptor

import com.rkm.tasky.BuildConfig
import com.rkm.tasky.network.authentication.abstraction.AuthenticationManager
import com.rkm.tasky.network.authorization.abstraction.AuthorizationManager
import com.rkm.tasky.network.datasource.TaskyAuthenticationRemoteDataSource
import com.rkm.tasky.network.datasource.TaskyAuthorizationRemoteDateSource
import com.rkm.tasky.network.fakes.AuthenticationManagerFake
import com.rkm.tasky.network.fakes.AuthenticationRepositoryFake
import com.rkm.tasky.network.fakes.SessionStorageFake
import com.rkm.tasky.network.model.request.RegisterUserRequest
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TaskyApiKeyInterceptorTest {

    private lateinit var server: MockWebServer
    private lateinit var okHttpClient: OkHttpClient
    private lateinit var retrofit: Retrofit
    private lateinit var dataSource: TaskyAuthenticationRemoteDataSource
    private lateinit var interceptor: TaskyApiKeyInterceptor
    private lateinit var repository: AuthenticationRepositoryFake
    private lateinit var storage: SessionStorageFake
    private lateinit var manager: AuthenticationManager

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start(8080)
        repository = AuthenticationRepositoryFake()
        storage = SessionStorageFake()
        manager = AuthenticationManagerFake(repository, storage)
        interceptor = TaskyApiKeyInterceptor()
        okHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()
        retrofit = Retrofit.Builder()
            .baseUrl(server.url(""))
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        dataSource = retrofit.create(TaskyAuthenticationRemoteDataSource::class.java)
    }

    @Test
    fun `confirming api key being added to request`() = runTest {
        val response = MockResponse().setResponseCode(200)
        server.enqueue(response)
        dataSource.registerUser(RegisterUserRequest("", "", ""))
        val request = server.takeRequest()
        assertEquals(request.getHeader("x-api-key"), BuildConfig.API_KEY)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }
}