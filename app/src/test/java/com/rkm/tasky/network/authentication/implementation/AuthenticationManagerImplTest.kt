package com.rkm.tasky.network.authentication.implementation

import com.rkm.tasky.network.model.dto.LoginDTO
import com.rkm.tasky.network.model.response.asLoginDTO
import com.rkm.tasky.network.repository.abstraction.AuthenticationRepository
import com.rkm.tasky.network.util.NetworkError
import com.rkm.tasky.resources.response.loginUserResponseToPojo
import com.rkm.tasky.util.result.Result
import com.rkm.tasky.util.storage.abstraction.SessionStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.stub
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class AuthenticationManagerImplTest {

    private val dispatcher = StandardTestDispatcher()
    private lateinit var authManager: AuthenticationManagerImpl
    private lateinit var authRepository: AuthenticationRepository
    private lateinit var sessionStorage: SessionStorage

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        authRepository = mock()
        sessionStorage = mock()
        authManager = AuthenticationManagerImpl(authRepository, sessionStorage, dispatcher)
    }

    @Test
    fun `login success`() = runTest(dispatcher) {
        val email = "email@gmail.com"
        val password = "password"
        whenever(authRepository.loginUser(any(), any())).thenReturn(Result.Success(
            loginUserResponseToPojo().asLoginDTO()
        ))
        val result = authManager.logIn(email, password)
        verify(sessionStorage).setSession(any())
        assertTrue(result is Result.Success)
    }

    @Test
    fun `login failed`() = runTest(dispatcher) {
        val email = "email@gmail.com"
        val password = "password"
        whenever(authRepository.loginUser(any(), any())).thenReturn(Result.Error(
            NetworkError.APIError.UNAUTHORIZED
        ))
        val result = authManager.logIn(email, password)
        assertTrue(result is Result.Error)
        assertTrue((result as Result.Error).error == NetworkError.APIError.UNAUTHORIZED)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}