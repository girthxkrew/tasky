package com.rkm.tasky.feature.loginscreen.viewmodel

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rkm.tasky.feature.error.errorToUiMessage
import com.rkm.tasky.network.authentication.abstraction.AuthenticationManager
import com.rkm.tasky.util.result.onFailure
import com.rkm.tasky.util.result.onSuccess
import com.rkm.tasky.util.validator.abstraction.EmailPatternValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val manager: AuthenticationManager,
    private val validator: EmailPatternValidator
) : ViewModel() {

    val email = TextFieldState()
    val password = TextFieldState()
    val isValidEmail by derivedStateOf {
        validator.isValidEmail(email.text.toString())
    }
    private val _showPassword = MutableStateFlow(false)
    val showPassword = _showPassword.asStateFlow()

    private val _loginScreenEventChannel = Channel<LoginScreenEvent>()
    val loginScreenEventChannel = _loginScreenEventChannel.receiveAsFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun onShowPasswordClicked() {
        _showPassword.update {
            !it
        }
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
                _loginScreenEventChannel.send(LoginScreenEvent.LoginFailedEvent(errorToUiMessage(error)))
                _isLoading.update { false }
            }
        }
    }
}

sealed interface LoginScreenEvent{
    data object LoginSuccessEvent : LoginScreenEvent
    data class LoginFailedEvent(
        val error: Int
    ): LoginScreenEvent
}
