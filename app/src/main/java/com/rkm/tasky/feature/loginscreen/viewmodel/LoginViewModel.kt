package com.rkm.tasky.feature.loginscreen.viewmodel

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import com.rkm.tasky.feature.error.errorToUiMessage
import com.rkm.tasky.network.authentication.abstraction.AuthenticationManager
import com.rkm.tasky.ui.component.UiText
import com.rkm.tasky.util.result.onFailure
import com.rkm.tasky.util.result.onSuccess
import com.rkm.tasky.util.validator.abstraction.EmailPatternValidator
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
class LoginViewModel @Inject constructor(
    private val manager: AuthenticationManager,
    private val validator: EmailPatternValidator,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private companion object {
        const val SHOW_PASSWORD_KEY = "show_password"
        const val PASSWORD_KEY = "password"
        const val EMAIL_KEY = "email"
    }

    val email = TextFieldState(savedStateHandle[EMAIL_KEY] ?: "")

    suspend fun setEmail() {
        snapshotFlow { email.text }.collectLatest { text ->
            savedStateHandle[EMAIL_KEY] = text
        }
    }

    val password = TextFieldState(savedStateHandle[PASSWORD_KEY] ?: "")

    suspend fun setPassword() {
        snapshotFlow { password.text }.collectLatest { text ->
            savedStateHandle[PASSWORD_KEY] = text
        }
    }

    val isValidEmail by derivedStateOf {
        validator.isValidEmail(email.text.toString())
    }
    private val _showPassword = MutableStateFlow(savedStateHandle.get<Boolean>(SHOW_PASSWORD_KEY) ?: false)
    val showPassword = _showPassword.asStateFlow()

    private val _loginScreenEventChannel = Channel<LoginScreenEvent>()
    val loginScreenEventChannel = _loginScreenEventChannel.receiveAsFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun onShowPasswordClicked() {
        _showPassword.update {
            !it
        }
        savedStateHandle[SHOW_PASSWORD_KEY] = showPassword.value
    }

    fun onLoginButtonClicked() {
        viewModelScope.launch {
            _isLoading.update { true }
            val result = manager.logIn(
                email = email.text.toString(),
                password = password.text.toString()
            )
            result.onSuccess {
                _loginScreenEventChannel.send(LoginScreenEvent.LoginSuccessEvent)
                _isLoading.update { false }
            }
            result.onFailure { error ->
                _loginScreenEventChannel.send(LoginScreenEvent.LoginFailedEvent(UiText.StringResource(errorToUiMessage(error))))
                _isLoading.update { false }
            }
        }
    }
}

sealed interface LoginScreenEvent{
    data object LoginSuccessEvent : LoginScreenEvent
    data class LoginFailedEvent(
        val message: UiText
    ): LoginScreenEvent
}
