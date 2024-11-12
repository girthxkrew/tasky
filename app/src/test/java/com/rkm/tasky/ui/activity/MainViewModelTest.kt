package com.rkm.tasky.ui.activity

import app.cash.turbine.test
import com.rkm.tasky.network.authentication.mapper.asSessionInfo
import com.rkm.tasky.network.fakes.AuthorizationManagerFake
import com.rkm.tasky.network.fakes.AuthorizationRepositoryFake
import com.rkm.tasky.network.fakes.SessionStorageFake
import com.rkm.tasky.resources.response.loginUserResponseToPojo
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
class MainViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private lateinit var manager: AuthorizationManagerFake
    private lateinit var repository: AuthorizationRepositoryFake
    private lateinit var sessionStorageFake: SessionStorageFake
    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        repository = AuthorizationRepositoryFake()
        sessionStorageFake = SessionStorageFake()
        manager = AuthorizationManagerFake(repository, sessionStorageFake)
    }

    @Test
    fun `user opens app for the first time so no session info saved`() = runTest {
        viewModel = MainViewModel(manager)
        viewModel.authState.test {
            assertEquals(awaitItem(), AuthState.Loading)
            assertEquals(awaitItem(), AuthState.NoSessionInfo)
        }
    }

    @Test
    fun `check for authentication with successful response`() = runTest {
        sessionStorageFake.setSession(loginUserResponseToPojo().asSessionInfo())
        viewModel = MainViewModel(manager)
        viewModel.authState.test {
            assertEquals(awaitItem(), AuthState.Loading)
            assertEquals(awaitItem(), AuthState.AuthenticatedUser)
        }
    }

    @Test
    fun `check for authentication with error response`() = runTest {
        sessionStorageFake.setSession(loginUserResponseToPojo().asSessionInfo())
        repository.setHasError(true)
        viewModel = MainViewModel(manager)
        viewModel.authState.test {
            assertEquals(awaitItem(), AuthState.Loading)
            assertEquals(awaitItem(), AuthState.Error)
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}