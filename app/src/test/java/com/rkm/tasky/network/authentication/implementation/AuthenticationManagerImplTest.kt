package com.rkm.tasky.network.authentication.implementation

import androidx.work.WorkManager
import com.rkm.tasky.network.fakes.AuthenticationRepositoryFake
import com.rkm.tasky.network.fakes.SessionStorageFake
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
import org.mockito.kotlin.mock

@OptIn(ExperimentalCoroutinesApi::class)
class AuthenticationManagerImplTest {

    private val dispatcher = StandardTestDispatcher()
    private lateinit var authManager: AuthenticationManagerImpl
    private lateinit var authRepository: AuthenticationRepositoryFake
    private lateinit var sessionStorage: SessionStorageFake
    private lateinit var workManager: WorkManager

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        workManager = mock()
        authRepository = AuthenticationRepositoryFake()
        sessionStorage = SessionStorageFake()
        authManager = AuthenticationManagerImpl(authRepository, sessionStorage, workManager ,dispatcher)
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

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}