package com.rkm.tasky.feature.registration.viewmodel

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import app.cash.turbine.turbineScope
import com.rkm.tasky.network.fakes.AuthenticationManagerFake
import com.rkm.tasky.network.fakes.AuthenticationRepositoryFake
import com.rkm.tasky.network.fakes.SessionStorageFake
import com.rkm.tasky.util.validator.implementation.EmailPatternValidator
import com.rkm.tasky.util.validator.implementation.NamePatternValidator
import com.rkm.tasky.util.validator.implementation.PasswordPatternValidator
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
class RegistrationViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private lateinit var manager: AuthenticationManagerFake
    private lateinit var repository: AuthenticationRepositoryFake
    private lateinit var emailValidator: EmailPatternValidator
    private lateinit var passwordValidator: PasswordPatternValidator
    private lateinit var nameValidator: NamePatternValidator
    private lateinit var viewModel: RegistrationViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        repository = AuthenticationRepositoryFake()
        manager = AuthenticationManagerFake(repository, SessionStorageFake())
        emailValidator = EmailPatternValidator()
        passwordValidator = PasswordPatternValidator()
        nameValidator = NamePatternValidator()
    }

    @Test
    fun `invalid name so error message is sent`() {
        val savedStateHandle = SavedStateHandle(
            mapOf(
                "email" to "email@email.com",
                "password" to "password",
                "name" to "ian",
                "show_password" to false
            )
        )
        viewModel = RegistrationViewModel(manager, savedStateHandle, emailValidator, passwordValidator, nameValidator)

        runTest {
            turbineScope {
                val registrationState = viewModel.registrationScreenEventChannel.testIn(backgroundScope)
                val isLoading = viewModel.isLoading.testIn(backgroundScope)
                assertFalse(isLoading.awaitItem())
                viewModel.onRegistrationClicked()
                val error = registrationState.awaitItem()
                assertTrue(error is RegistrationScreenEvent.RegistrationFailedEvent)
                assertFalse(viewModel.isValidName)
            }
        }
    }

    @Test
    fun `invalid password so error message is sent`() {
        val savedStateHandle = SavedStateHandle(
            mapOf(
                "email" to "email@email.com",
                "password" to "password",
                "name" to "bob smith",
                "show_password" to false
            )
        )
        viewModel = RegistrationViewModel(manager, savedStateHandle, emailValidator, passwordValidator, nameValidator)

        runTest {
            turbineScope {
                val registrationState = viewModel.registrationScreenEventChannel.testIn(backgroundScope)
                val isLoading = viewModel.isLoading.testIn(backgroundScope)
                assertFalse(isLoading.awaitItem())
                viewModel.onRegistrationClicked()
                assertTrue(registrationState.awaitItem() is RegistrationScreenEvent.RegistrationFailedEvent)
                assertTrue(viewModel.isValidName)
                assertTrue(viewModel.isValidEmail)
            }
        }
    }

    @Test
    fun `invalid email so error message is sent`() {
        val savedStateHandle = SavedStateHandle(
            mapOf(
                "email" to "email",
                "password" to "Password1",
                "name" to "bob smith",
                "show_password" to false
            )
        )
        viewModel = RegistrationViewModel(manager, savedStateHandle, emailValidator, passwordValidator, nameValidator)

        runTest {
            turbineScope {
                val registrationState = viewModel.registrationScreenEventChannel.testIn(backgroundScope)
                val isLoading = viewModel.isLoading.testIn(backgroundScope)
                assertFalse(isLoading.awaitItem())
                viewModel.onRegistrationClicked()
                assertTrue(registrationState.awaitItem() is RegistrationScreenEvent.RegistrationFailedEvent)
                assertFalse(viewModel.isValidEmail)
            }
        }
    }

    @Test
    fun `valid email, password, name and valid register response`() {
        val savedStateHandle = SavedStateHandle(
            mapOf(
                "email" to "email@email.com",
                "password" to "Password1",
                "name" to "bob smith",
                "show_password" to false
            )
        )
        viewModel = RegistrationViewModel(manager, savedStateHandle, emailValidator, passwordValidator, nameValidator)

        runTest {
            turbineScope {
                val registrationState = viewModel.registrationScreenEventChannel.testIn(backgroundScope)
                val isLoading = viewModel.isLoading.testIn(backgroundScope)
                assertFalse(isLoading.awaitItem())
                viewModel.onRegistrationClicked()
                assertTrue(isLoading.awaitItem())
                assertTrue(registrationState.awaitItem() is RegistrationScreenEvent.RegistrationSuccessEvent)
                assertFalse(isLoading.awaitItem())
                assertTrue(viewModel.isValidEmail)
                assertTrue(viewModel.isValidName)
            }
        }
    }

    @Test
    fun `valid email, password, name and invalid register response`() {
        val savedStateHandle = SavedStateHandle(
            mapOf(
                "email" to "email@email.com",
                "password" to "Password1",
                "name" to "bob smith",
                "show_password" to false
            )
        )
        repository.setHasError(true)
        viewModel = RegistrationViewModel(manager, savedStateHandle, emailValidator, passwordValidator, nameValidator)

        runTest {
            turbineScope {
                val registrationState = viewModel.registrationScreenEventChannel.testIn(backgroundScope)
                val isLoading = viewModel.isLoading.testIn(backgroundScope)
                assertFalse(isLoading.awaitItem())
                viewModel.onRegistrationClicked()
                assertTrue(isLoading.awaitItem())
                assertTrue(registrationState.awaitItem() is RegistrationScreenEvent.RegistrationFailedEvent)
                assertFalse(isLoading.awaitItem())
                assertTrue(viewModel.isValidEmail)
                assertTrue(viewModel.isValidName)
            }
        }
    }

    @Test
    fun`verify showPassword is set to true when OnShowPasswordCalled()`() {
        val savedStateHandle = SavedStateHandle()
        viewModel = RegistrationViewModel(manager, savedStateHandle, emailValidator, passwordValidator, nameValidator)

        runTest {
            viewModel.showPassword.test {
                assertFalse(awaitItem())
                viewModel.onShowPasswordClicked()
                assertTrue(awaitItem())
                cancelAndIgnoreRemainingEvents()
            }

        }
    }

    @Test
    fun`verify showPassword is set to false when OnShowPasswordCalled() is clicked twice`() {
        val savedStateHandle = SavedStateHandle()
        viewModel = RegistrationViewModel(manager, savedStateHandle, emailValidator, passwordValidator, nameValidator)

        runTest {
            viewModel.showPassword.test {
                assertFalse(awaitItem())
                viewModel.onShowPasswordClicked()
                assertTrue(awaitItem())
                viewModel.onShowPasswordClicked()
                assertFalse(awaitItem())
                cancelAndIgnoreRemainingEvents()
            }

        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}