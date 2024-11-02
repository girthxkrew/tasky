package com.rkm.tasky.feature.login.viewmodel

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import app.cash.turbine.turbineScope
import com.rkm.tasky.network.authentication.abstraction.AuthenticationManager
import com.rkm.tasky.network.fakes.AuthenticationManagerFake
import com.rkm.tasky.network.fakes.AuthenticationRepositoryFake

import com.rkm.tasky.network.fakes.SessionStorageFake
import com.rkm.tasky.util.validator.implementation.EmailPatternValidator
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
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var manager: AuthenticationManager
    private lateinit var validator: EmailPatternValidator
    private lateinit var repository: AuthenticationRepositoryFake

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        repository = AuthenticationRepositoryFake()
        manager = AuthenticationManagerFake(repository, SessionStorageFake())
        validator = EmailPatternValidator()
    }

    @Test
    fun`verify when valid email that isValidEmail is set to true`() {
        val savedStateHandle = SavedStateHandle(
            mapOf(
                "email" to "email@email.com",
                "password" to "password",
                "show_password" to true
            )
        )
        loginViewModel = LoginViewModel(manager, validator, savedStateHandle)
        assertTrue(loginViewModel.isValidEmail)
    }

    @Test
    fun`verify when invalid email that isValidEmail is set to false`() {
        val savedStateHandle = SavedStateHandle(
            mapOf(
                "email" to "",
                "password" to "password",
                "show_password" to true
            )
        )
        loginViewModel = LoginViewModel(manager, validator, savedStateHandle)
        assertFalse(loginViewModel.isValidEmail)
    }

    @Test
    fun`verify showPassword is set to true when OnShowPasswordCalled()`() {
        val savedStateHandle = SavedStateHandle()
        loginViewModel = LoginViewModel(manager, validator, savedStateHandle)

        runTest {
            loginViewModel.showPassword.test {
                assertFalse(awaitItem())
                loginViewModel.onShowPasswordClicked()
                assertTrue(awaitItem())
                cancelAndIgnoreRemainingEvents()
            }

        }
    }

    @Test
    fun`verify showPassword is set to false when OnShowPasswordCalled() is clicked twice`() {
        val savedStateHandle = SavedStateHandle()
        loginViewModel = LoginViewModel(manager, validator, savedStateHandle)

        runTest {
            loginViewModel.showPassword.test {
                assertFalse(awaitItem())
                loginViewModel.onShowPasswordClicked()
                assertTrue(awaitItem())
                loginViewModel.onShowPasswordClicked()
                assertFalse(awaitItem())
                cancelAndIgnoreRemainingEvents()
            }

        }
    }

    @Test
    fun`verify login success when login clicked`() {
        val savedStateHandle = SavedStateHandle()
        loginViewModel = LoginViewModel(manager, validator, savedStateHandle)
        runTest {
            turbineScope {
                val isLoadingState = loginViewModel.isLoading.testIn(backgroundScope)
                val loginState = loginViewModel.loginScreenEventChannel.testIn(backgroundScope)
                assertFalse(isLoadingState.awaitItem())
                loginViewModel.onLoginButtonClicked()
                assertTrue(isLoadingState.awaitItem())
                assertTrue(loginState.awaitItem() is LoginScreenEvent.LoginSuccessEvent)
                assertFalse(isLoadingState.awaitItem())
            }
        }
    }

    @Test
    fun`verify login fail when login clicked`() {
        val savedStateHandle = SavedStateHandle()
        repository.setHasError(true)
        loginViewModel = LoginViewModel(manager, validator, savedStateHandle)
        runTest {
            turbineScope {
                val isLoadingState = loginViewModel.isLoading.testIn(backgroundScope)
                val loginState = loginViewModel.loginScreenEventChannel.testIn(backgroundScope)
                assertFalse(isLoadingState.awaitItem())
                loginViewModel.onLoginButtonClicked()
                assertTrue(isLoadingState.awaitItem())
                assertTrue(loginState.awaitItem() is LoginScreenEvent.LoginFailedEvent)
                assertFalse(isLoadingState.awaitItem())
            }
        }
    }

    @After
    fun tearDown() {
       Dispatchers.resetMain()
    }
}