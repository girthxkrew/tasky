package com.rkm.tasky.network.authorization.implementation

import com.rkm.tasky.network.fakes.AuthorizationRepositoryFake
import com.rkm.tasky.network.fakes.SessionStorageFake
import com.rkm.tasky.network.model.dto.asSessionInfo
import com.rkm.tasky.network.model.response.asAccessTokenDTO
import com.rkm.tasky.network.model.response.asLoginDTO
import com.rkm.tasky.network.util.NetworkError
import com.rkm.tasky.resources.response.getNewAccessTokenResponseToPojo
import com.rkm.tasky.resources.response.loginUserResponseToPojo
import com.rkm.tasky.util.result.Result
import com.rkm.tasky.util.storage.model.asAuthInfoDTO
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class AuthorizationManagerImplTest {

    private val dispatcher = StandardTestDispatcher()
    private lateinit var authManager: AuthorizationManagerImpl
    private lateinit var authRepository: AuthorizationRepositoryFake
    private lateinit var sessionStorage: SessionStorageFake

    @Before
    fun setUp() {
        authRepository = AuthorizationRepositoryFake()
        sessionStorage = SessionStorageFake()
        authManager = AuthorizationManagerImpl(authRepository, sessionStorage, dispatcher)
    }

    @Test
    fun `logout success`() = runTest(dispatcher) {
        sessionStorage.setSession(loginUserResponseToPojo().asLoginDTO().asSessionInfo())
        assertTrue(sessionStorage.getSession() != null)
        val result2 = authManager.logOut()
        assertTrue(result2 is Result.Success)
        assertTrue(sessionStorage.getSession() == null)
    }

    @Test
    fun `logout failed`() = runTest(dispatcher) {
        sessionStorage.setSession(loginUserResponseToPojo().asLoginDTO().asSessionInfo())
        assertTrue(sessionStorage.getSession() != null)
        authRepository.setHasError(true)
        val result2 = authManager.logOut()
        assertTrue(result2 is Result.Error)
        assertTrue(sessionStorage.getSession() != null)
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
    fun `get new access token session empty`() = runTest(dispatcher) {
        val result = authManager.getNewAccessToken()
        assertTrue(result is Result.Error)
        assertEquals((result as Result.Error).error, NetworkError.APIError.NO_SESSION_INFO)
    }

    @Test
    fun `get new access token session success`() = runTest(dispatcher) {
        val authInfo = loginUserResponseToPojo().asLoginDTO().asSessionInfo()
        val tokenResponse = getNewAccessTokenResponseToPojo().asAccessTokenDTO()
        sessionStorage.setSession(authInfo)
        val updatedAuthInfo = authInfo.copy(
            accessToken = tokenResponse.accessToken,
            accessTokenExpirationTimestamp = tokenResponse.expirationTimestamp
        )
        val result2 = authManager.getNewAccessToken()
        assertTrue(result2 is Result.Success)
        assertEquals(sessionStorage.getSession(), updatedAuthInfo)
    }

    @Test
    fun `get new access token session failed`() = runTest(dispatcher) {
        val authInfo = loginUserResponseToPojo().asLoginDTO().asSessionInfo()
        val tokenResponse = getNewAccessTokenResponseToPojo().asAccessTokenDTO()
        sessionStorage.setSession(authInfo)
        val updatedAuthInfo = authInfo.copy(
            accessToken = tokenResponse.accessToken,
            accessTokenExpirationTimestamp = tokenResponse.expirationTimestamp
        )
        authRepository.setHasError(true)
        val result2 = authManager.getNewAccessToken()
        assertTrue(result2 is Result.Error)
        assertNotEquals(sessionStorage.getSession(), updatedAuthInfo)
        assertEquals(sessionStorage.getSession(), authInfo)
    }

    @Test
    fun `get session success`() = runTest(dispatcher) {
        val authInfo = loginUserResponseToPojo().asLoginDTO().asSessionInfo()
        sessionStorage.setSession(authInfo)
        assertTrue(authManager.getSessionInfo() == authInfo.asAuthInfoDTO())
    }

    @Test
    fun `get session failed`() = runTest(dispatcher) {
        assertTrue(authManager.getSessionInfo() == null)
    }
}