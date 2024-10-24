package com.rkm.tasky.network.repository.implementation

import com.rkm.tasky.network.datasource.TaskyRemoteDataSource
import com.rkm.tasky.network.model.request.RegisterUserRequest
import com.rkm.tasky.network.model.response.asAccessTokenDTO
import com.rkm.tasky.network.model.response.asLoginDTO
import com.rkm.tasky.resources.response.errorMessageToString
import com.rkm.tasky.network.util.NetworkError
import com.rkm.tasky.resources.response.getNewAccessTokenResponseToPojo
import com.rkm.tasky.resources.response.getNewAccessTokenResponseToString
import com.rkm.tasky.resources.response.loginUserResponseToPojo
import com.rkm.tasky.resources.response.loginUserResponseToString
import com.rkm.tasky.util.result.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@OptIn(ExperimentalCoroutinesApi::class)
class AuthenticationRepositoryImplTest {

    private lateinit var server: MockWebServer
    private lateinit var authRepository: AuthenticationRepositoryImpl
    private lateinit var dataSource: TaskyRemoteDataSource
    private lateinit var retrofit: Retrofit
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        server = MockWebServer()
        server.start(8080)
        retrofit = Retrofit.Builder()
            .baseUrl(server.url(""))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        dataSource = retrofit.create(TaskyRemoteDataSource::class.java)
        authRepository = AuthenticationRepositoryImpl(dataSource, dispatcher)

    }

    @Test
    fun `register user successful`() = runTest {
        val name = "Bob Smith"
        val email = "email@gmail.com"
        val password = "password"
        val mockResponse = MockResponse().setResponseCode(200)
        server.enqueue(mockResponse)
        val result = authRepository.registerUser(name, email, password)
        runCurrent()
        assertTrue(result is Result.Success)
    }

    @Test
    fun `register user failure`() = runTest {
        val name = "Bob Smith"
        val email = "email@gmail.com"
        val password = "password"
        val mockResponse = MockResponse().setResponseCode(401).setBody(errorMessageToString())
        server.enqueue(mockResponse)
        val result = authRepository.registerUser(name, email, password)
        runCurrent()
        assertTrue(result is Result.Error)
        assertTrue((result as Result.Error).error == NetworkError.APIError.UNAUTHORIZED)
    }

    @Test
    fun `login successful`() = runTest {
        val email = "email@gmail.com"
        val password = "password"
        val mockResponse = MockResponse().setResponseCode(200).setBody(loginUserResponseToString())
        server.enqueue(mockResponse)
        val result = authRepository.loginUser(email, password)
        runCurrent()
        assertTrue(result is Result.Success)
        assertTrue((result as Result.Success).data == loginUserResponseToPojo().asLoginDTO())
    }

    @Test
    fun `login failure`() = runTest {
        val email = "email@gmail.com"
        val password = "password"
        val mockResponse = MockResponse().setResponseCode(401).setBody(errorMessageToString())
        server.enqueue(mockResponse)
        val result = authRepository.loginUser(email, password)
        runCurrent()
        assertTrue(result is Result.Error)
        assertTrue((result as Result.Error).error == NetworkError.APIError.UNAUTHORIZED)
    }

    @Test
    fun `logout successful`() = runTest {
        val mockResponse = MockResponse().setResponseCode(200)
        server.enqueue(mockResponse)
        val result = authRepository.logoutUser()
        runCurrent()
        assertTrue(result is Result.Success)
    }

    @Test
    fun `logout failure`() = runTest {
        val mockResponse = MockResponse().setResponseCode(401).setBody(errorMessageToString())
        server.enqueue(mockResponse)
        val result = authRepository.logoutUser()
        runCurrent()
        assertTrue(result is Result.Error)
        assertTrue((result as Result.Error).error == NetworkError.APIError.UNAUTHORIZED)
    }

    @Test
    fun `check authentication successful`() = runTest {
        val mockResponse = MockResponse().setResponseCode(200)
        server.enqueue(mockResponse)
        val result = authRepository.checkAuthentication()
        runCurrent()
        assertTrue(result is Result.Success)
    }

    @Test
    fun `check authentication failure`() = runTest {
        val mockResponse = MockResponse().setResponseCode(401).setBody(errorMessageToString())
        server.enqueue(mockResponse)
        val result = authRepository.checkAuthentication()
        runCurrent()
        assertTrue(result is Result.Error)
        assertTrue((result as Result.Error).error == NetworkError.APIError.UNAUTHORIZED)
    }

    @Test
    fun `get new access token successful`() = runTest {
        val mockResponse = MockResponse().setResponseCode(200).setBody(
            getNewAccessTokenResponseToString()
        )
        server.enqueue(mockResponse)
        val result = authRepository.getNewAccessToken("", "")
        runCurrent()
        assertTrue(result is Result.Success)
        assertTrue((result as Result.Success).data == getNewAccessTokenResponseToPojo().asAccessTokenDTO())
    }

    @Test
    fun `get new access token failure`() = runTest {
        val mockResponse = MockResponse().setResponseCode(401).setBody(errorMessageToString())
        server.enqueue(mockResponse)
        val result = authRepository.getNewAccessToken("", "")
        runCurrent()
        assertTrue(result is Result.Error)
        assertTrue((result as Result.Error).error == NetworkError.APIError.UNAUTHORIZED)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        server.shutdown()
    }
}