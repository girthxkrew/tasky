package com.rkm.tasky.feature.registration.viewmodel

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rkm.tasky.R
import com.rkm.tasky.feature.error.errorToUiMessage
import com.rkm.tasky.network.authentication.abstraction.AuthenticationManager
import com.rkm.tasky.ui.component.UiText
import com.rkm.tasky.util.result.onFailure
import com.rkm.tasky.util.result.onSuccess
import com.rkm.tasky.util.validator.abstraction.EmailPatternValidator
import com.rkm.tasky.util.validator.abstraction.NamePatternValidator
import com.rkm.tasky.util.validator.abstraction.PasswordPatternValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val manager: AuthenticationManager,
    private val savedStateHandle: SavedStateHandle,
    private val emailValidator: EmailPatternValidator,
    private val passwordValidator: PasswordPatternValidator,
    private val nameValidator: NamePatternValidator
) : ViewModel() {

    private companion object {
        const val EMAIL_KEY = "email"
        const val PASSWORD_KEY = "password"
        const val NAME_KEY = "name"
        const val SHOW_PASSWORD_KEY = "show_password"
    }

    private val mapErrors = mapOf(
        EMAIL_KEY to R.string.registration_screen_invalid_email,
        PASSWORD_KEY to R.string.registration_screen_invalid_password,
        NAME_KEY to R.string.registration_screen_invalid_name
    )

    val email = TextFieldState(savedStateHandle[EMAIL_KEY] ?: "")
    suspend fun setEmail() {
        snapshotFlow { email.text }.collectLatest { text ->
            savedStateHandle[EMAIL_KEY] = text
        }
    }

    val isValidEmail by derivedStateOf {
        emailValidator.isValidEmail(email.text.toString())
    }
    val password = TextFieldState(savedStateHandle[PASSWORD_KEY] ?: "")

    private val isValidPassword by derivedStateOf {
        passwordValidator.isValidPassword(password.text.toString())
    }
    val fullName = TextFieldState(savedStateHandle[NAME_KEY] ?: "")
    suspend fun setName() {
        snapshotFlow { fullName.text }.collectLatest { text ->
            savedStateHandle[NAME_KEY] = text
        }
    }

    val isValidName by derivedStateOf {
        nameValidator.isValidName(fullName.text.toString())
    }
    private val _showPassword = MutableStateFlow(savedStateHandle[SHOW_PASSWORD_KEY] ?: false)
    val showPassword = _showPassword.asStateFlow()
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    private val _registrationScreenEventChannel = Channel<RegistrationScreenEvent>()
    val registrationScreenEventChannel = _registrationScreenEventChannel.receiveAsFlow()

    fun onShowPasswordClicked() {
        _showPassword.update {
            !it
        }
    }

    fun onRegistrationClicked() {
        viewModelScope.launch {
            if (!isValidName || !isValidPassword || !isValidEmail) {
                _registrationScreenEventChannel.send(
                    if (!isValidName) RegistrationScreenEvent.RegistrationFailedEvent(
                        UiText.StringResource(
                            mapErrors[NAME_KEY]!!
                        )
                    )
                    else if (!isValidEmail) RegistrationScreenEvent.RegistrationFailedEvent(
                        UiText.StringResource(
                            mapErrors[EMAIL_KEY]!!
                        )
                    )
                    else RegistrationScreenEvent.RegistrationFailedEvent(
                        UiText.StringResource(
                            mapErrors[PASSWORD_KEY]!!
                        )
                    )
                )
                return@launch
            }
            _isLoading.update { true }
            val result = manager.registerUser(
                fullName = fullName.text.toString(),
                email = email.text.toString(),
                password = password.text.toString()
            )
            result.onSuccess {
                _isLoading.update { false }
                _registrationScreenEventChannel.send(RegistrationScreenEvent.RegistrationSuccessEvent)
            }
            result.onFailure { error ->
                _isLoading.update { false }
                _registrationScreenEventChannel.send(
                    RegistrationScreenEvent.RegistrationFailedEvent(
                        UiText.StringResource(errorToUiMessage(error))
                    )
                )
            }
        }
    }
}

sealed interface RegistrationScreenEvent {
    data object RegistrationSuccessEvent : RegistrationScreenEvent
    data class RegistrationFailedEvent(
        val message: UiText
    ) : RegistrationScreenEvent
}