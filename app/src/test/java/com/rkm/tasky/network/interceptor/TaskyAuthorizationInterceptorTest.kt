package com.rkm.tasky.network.interceptor

import com.rkm.tasky.BuildConfig
import com.rkm.tasky.network.authentication.abstraction.AuthenticationManager
import com.rkm.tasky.network.authorization.abstraction.AuthorizationManager
import com.rkm.tasky.network.datasource.TaskyAuthenticationRemoteDataSource
import com.rkm.tasky.network.datasource.TaskyAuthorizationRemoteDateSource
import com.rkm.tasky.network.fakes.AuthenticationManagerFake
import com.rkm.tasky.network.fakes.AuthenticationRepositoryFake
import com.rkm.tasky.network.fakes.AuthorizationManagerFake
import com.rkm.tasky.network.fakes.AuthorizationRepositoryFake
import com.rkm.tasky.network.fakes.SessionStorageFake
import com.rkm.tasky.network.model.dto.asSessionInfo
import com.rkm.tasky.network.model.request.RegisterUserRequest
import com.rkm.tasky.network.model.response.asLoginDTO
import com.rkm.tasky.resources.response.loginUserResponseToPojo
import dagger.Lazy
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

class TaskyAuthorizationInterceptorTest {

    private lateinit var server: MockWebServer
    private lateinit var okHttpClient: OkHttpClient
    private lateinit var retrofit: Retrofit
    private lateinit var authorizationDataSource: TaskyAuthorizationRemoteDateSource
    private lateinit var authenticationDataSource: TaskyAuthenticationRemoteDataSource
    private lateinit var authInterceptor: TaskyAuthorizationInterceptor
    private lateinit var apiKeyInterceptor: TaskyApiKeyInterceptor
    private lateinit var authorizationRepository: AuthorizationRepositoryFake
    private lateinit var authenticationRepository: AuthenticationRepositoryFake
    private lateinit var storage: SessionStorageFake
    private lateinit var authorizationManager: AuthorizationManager
    private lateinit var authenticationManager: AuthenticationManager

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start(8080)
        authorizationRepository = AuthorizationRepositoryFake()
        authenticationRepository = AuthenticationRepositoryFake()
        storage = SessionStorageFake()
        authorizationManager = AuthorizationManagerFake(authorizationRepository, storage)
        authenticationManager = AuthenticationManagerFake(authenticationRepository, storage)
        val lazyManager = Lazy { authorizationManager }
        authInterceptor = TaskyAuthorizationInterceptor(lazyManager)
        apiKeyInterceptor = TaskyApiKeyInterceptor()
        okHttpClient = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(apiKeyInterceptor)
            .build()
        retrofit = Retrofit.Builder()
            .baseUrl(server.url(""))
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        authorizationDataSource = retrofit.create(TaskyAuthorizationRemoteDateSource::class.java)
        authenticationDataSource = retrofit.create(TaskyAuthenticationRemoteDataSource::class.java)
    }

    @Test
    fun `adding api key + authorization to request`() = runTest {
        storage.setSession(loginUserResponseToPojo().asLoginDTO().asSessionInfo())
        val response = MockResponse().setResponseCode(200)
        server.enqueue(response)
        authorizationDataSource.logoutUser()
        val request = server.takeRequest()
        assertEquals(
            request.getHeader("Authorization"),
            "Bearer ${storage.getSession()!!.accessToken}"
        )
    }

    @Test
    fun `adding api key + authorization to request but with new access token success`() = runTest {
        storage.setSession(loginUserResponseToPojo().asLoginDTO().asSessionInfo())
        server.enqueue(MockResponse().setBody("Authorized").setResponseCode(401))
        server.enqueue(MockResponse().setResponseCode(200))
        authorizationDataSource.logoutUser()
        server.takeRequest()
        val request2 = server.takeRequest()
        assertEquals(
            request2.getHeader("Authorization"),
            "Bearer ${storage.getSession()!!.accessToken}"
        )
    }

    @Test
    fun `adding api key + authorization to request but with new access token failed`() = runTest {
        authorizationRepository.setHasError(true)
        storage.setSession(loginUserResponseToPojo().asLoginDTO().asSessionInfo())
        server.enqueue(MockResponse().setBody("Authorized").setResponseCode(401))
        authorizationDataSource.logoutUser()
        val request = server.takeRequest()
        assertEquals(
            request.getHeader("Authorization"),
            "Bearer ${storage.getSession()!!.accessToken}"
        )
    }

    @Test
    fun `confirming api key + authorization header`() = runTest {
        storage.setSession(loginUserResponseToPojo().asLoginDTO().asSessionInfo())
        val response = MockResponse().setResponseCode(200)
        server.enqueue(response)
        authorizationDataSource.logoutUser()
        val request = server.takeRequest()
        assertEquals(
            request.getHeader("Authorization"),
            "Bearer ${storage.getSession()!!.accessToken}"
        )
        assertEquals(request.getHeader("x-api-key"), BuildConfig.API_KEY)
    }

    @Test
    fun `confirming authorization header is not added to specific call`() = runTest {
        val response = MockResponse().setResponseCode(200)
        server.enqueue(response)
        authenticationDataSource.registerUser(RegisterUserRequest("", "", ""))
        val request = server.takeRequest()
        assertEquals(request.getHeader("x-api-key"), BuildConfig.API_KEY)
        assertEquals(request.getHeader("Authorization"), null)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }
}