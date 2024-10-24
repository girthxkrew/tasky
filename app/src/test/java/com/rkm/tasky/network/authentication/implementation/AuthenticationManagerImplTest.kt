package com.rkm.tasky.network.authentication.implementation

import com.rkm.tasky.network.authentication.fakes.AuthenticationRepositoryFake
import com.rkm.tasky.network.authentication.fakes.SessionStorageFake
import com.rkm.tasky.network.model.dto.asAuthInfo
import com.rkm.tasky.network.model.response.asAccessTokenDTO
import com.rkm.tasky.network.model.response.asLoginDTO
import com.rkm.tasky.network.util.NetworkError
import com.rkm.tasky.resources.response.getNewAccessTokenResponseToPojo
import com.rkm.tasky.resources.response.loginUserResponseToPojo
import com.rkm.tasky.util.result.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AuthenticationManagerImplTest {

    private val dispatcher = StandardTestDispatcher()
    private lateinit var authManager: AuthenticationManagerImpl
    private lateinit var authRepository: AuthenticationRepositoryFake
    private lateinit var sessionStorage: SessionStorageFake

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        authRepository = AuthenticationRepositoryFake()
        sessionStorage = SessionStorageFake()
        authManager = AuthenticationManagerImpl(authRepository, sessionStorage, dispatcher)
    }

    @Test
    fun `login success`() = runTest(dispatcher) {
        val email = "email@gmail.com"
        val password = "password"
        val result = authManager.logIn(email, password)
        assertTrue(result is Result.Success)
        assertTrue(sessionStorage.getSession() != null)
    }

    @Test
    fun `login failed`() = runTest(dispatcher) {
        val email = "email@gmail.com"
        val password = "password"
        authRepository.setHasError(true)
        val result = authManager.logIn(email, password)
        assertTrue(result is Result.Error)
        assertTrue(sessionStorage.getSession() == null)
    }

    @Test
    fun `logout success`() = runTest(dispatcher) {
        val email = "email@gmail.com"
        val password = "password"
        val result = authManager.logIn(email, password)
        assertTrue(result is Result.Success)
        assertTrue(sessionStorage.getSession() != null)
        val result2 = authManager.logOut()
        assertTrue(result2 is Result.Success)
        assertTrue(sessionStorage.getSession() == null)
    }

    @Test
    fun `logout failed`() = runTest(dispatcher) {
        val email = "email@gmail.com"
        val password = "password"
        val result = authManager.logIn(email, password)
        assertTrue(result is Result.Success)
        assertTrue(sessionStorage.getSession() != null)
        authRepository.setHasError(true)
        val result2 = authManager.logOut()
        assertTrue(result2 is Result.Error)
        assertTrue(sessionStorage.getSession() != null)
    }

    @Test
    fun `registration success`() = runTest(dispatcher) {
        val email = "email@gmail.com"
        val password = "password"
        val fullName = "Bob Smith"
        val result = authManager.registerUser(fullName, email, password)
        assertTrue(result is Result.Success)
    }

    @Test
    fun `registration failed`() = runTest(dispatcher) {
        val email = "email@gmail.com"
        val password = "password"
        val fullName = "Bob Smith"
        authRepository.setHasError(true)
        val result = authManager.registerUser(fullName, email, password)
        assertTrue(result is Result.Error)
    }

    @Test
    fun `check authentication success`() = runTest(dispatcher) {
        val result = authManager.checkAuthentication()
        assertTrue(result is Result.Success)
    }

    @Test
    fun `check authentication failed`() = runTest(dispatcher) {
        authRepository.setHasError(true)
        val result = authManager.checkAuthentication()
        assertTrue(result is Result.Error)
    }

    @Test
    fun `get new access token session empty`() = runTest {
        val result = authManager.getNewAccessToken()
        assertTrue(result is Result.Error)
        assertEquals((result as Result.Error).error, NetworkError.APIError.UNKNOWN)
    }

    @Test
    fun `get new access token session success`() = runTest {
        val email = "email@gmail.com"
        val password = "password"
        val authInfo = loginUserResponseToPojo().asLoginDTO().asAuthInfo()
        val tokenResponse = getNewAccessTokenResponseToPojo().asAccessTokenDTO()
        val updatedAuthInfo = authInfo.copy(
            accessToken = tokenResponse.accessToken,
            accessTokenExpirationTimestamp = tokenResponse.expirationTimestamp
        )
        val result = authManager.logIn(email, password)
        assertTrue(result is Result.Success)
        assertTrue(sessionStorage.getSession() != null)
        val result2 = authManager.getNewAccessToken()
        assertTrue(result2 is Result.Success)
        assertEquals(sessionStorage.getSession(), updatedAuthInfo)
    }

    @Test
    fun `get new access token session failed`() = runTest {
        val email = "email@gmail.com"
        val password = "password"
        val authInfo = loginUserResponseToPojo().asLoginDTO().asAuthInfo()
        val tokenResponse = getNewAccessTokenResponseToPojo().asAccessTokenDTO()
        val updatedAuthInfo = authInfo.copy(
            accessToken = tokenResponse.accessToken,
            accessTokenExpirationTimestamp = tokenResponse.expirationTimestamp
        )
        val result = authManager.logIn(email, password)
        assertTrue(result is Result.Success)
        assertTrue(sessionStorage.getSession() != null)
        authRepository.setHasError(true)
        val result2 = authManager.getNewAccessToken()
        assertTrue(result2 is Result.Error)
        assertNotEquals(sessionStorage.getSession(), updatedAuthInfo)
        assertEquals(sessionStorage.getSession(), authInfo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}