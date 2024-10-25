package com.rkm.tasky.network.interceptor

import com.rkm.tasky.BuildConfig
import com.rkm.tasky.network.datasource.TaskyRemoteDataSource
import com.rkm.tasky.network.fakes.AuthorizationManagerFake
import com.rkm.tasky.network.fakes.AuthorizationRepositoryFake
import com.rkm.tasky.network.fakes.SessionStorageFake
import com.rkm.tasky.network.model.dto.asSessionInfo
import com.rkm.tasky.network.model.request.RegisterUserRequest
import com.rkm.tasky.network.model.response.asLoginDTO
import com.rkm.tasky.resources.response.loginUserResponseToPojo
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TaskyInterceptorTest {

    private lateinit var server: MockWebServer
    private lateinit var okHttpClient: OkHttpClient
    private lateinit var retrofit: Retrofit
    private lateinit var dataSource: TaskyRemoteDataSource
    private lateinit var interceptor: TaskyInterceptor
    private lateinit var repository: AuthorizationRepositoryFake
    private lateinit var storage: SessionStorageFake
    private lateinit var manager: AuthorizationManagerFake

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start(8080)
        repository = AuthorizationRepositoryFake()
        storage = SessionStorageFake()
        manager = AuthorizationManagerFake(repository, storage)
        interceptor = TaskyInterceptor(manager)
        okHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()
        retrofit = Retrofit.Builder()
            .baseUrl(server.url(""))
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        dataSource = retrofit.create(TaskyRemoteDataSource::class.java)
    }

    @Test
    fun `only adding api key to request`() = runTest {
        val response = MockResponse().setResponseCode(200)
        server.enqueue(response)
        dataSource.registerUser(RegisterUserRequest("", "", ""))
        val request = server.takeRequest()
        assertEquals(request.getHeader("x-api-key"), BuildConfig.API_KEY)
    }

    @Test
    fun `adding api key + authorization to request`() = runTest {
        storage.setSession(loginUserResponseToPojo().asLoginDTO().asSessionInfo())
        val response = MockResponse().setResponseCode(200)
        server.enqueue(response)
        dataSource.logoutUser()
        val request = server.takeRequest()
        assertEquals(request.getHeader("x-api-key"), BuildConfig.API_KEY)
        assertEquals(request.getHeader("Authorization"), "Bearer ${storage.getSession()!!.accessToken}")
    }

    @Test
    fun `adding api key + authorization to request but with new access token success`() = runTest {
        storage.setSession(loginUserResponseToPojo().asLoginDTO().asSessionInfo())
        server.enqueue(MockResponse().setBody("Authorized").setResponseCode(401))
        server.enqueue(MockResponse().setResponseCode(200))
        dataSource.logoutUser()
        server.takeRequest()
        val request2 = server.takeRequest()
        assertEquals(request2.getHeader("x-api-key"), BuildConfig.API_KEY)
        assertEquals(request2.getHeader("Authorization"), "Bearer ${storage.getSession()!!.accessToken}")
    }

    @Test
    fun `adding api key + authorization to request but with new access token failed`() = runTest {
        repository.setHasError(true)
        storage.setSession(loginUserResponseToPojo().asLoginDTO().asSessionInfo())
        server.enqueue(MockResponse().setBody("Authorized").setResponseCode(401))
        dataSource.logoutUser()
        val request = server.takeRequest()
        assertEquals(request.getHeader("x-api-key"), BuildConfig.API_KEY)
        assertEquals(request.getHeader("Authorization"), "Bearer ${storage.getSession()!!.accessToken}")
    }

    @After
    fun tearDown() {
        server.shutdown()
    }
}