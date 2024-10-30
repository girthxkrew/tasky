package com.rkm.tasky.feature.loginscreen.viewmodel

import android.util.Patterns
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rkm.tasky.network.authentication.abstraction.AuthenticationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val manager: AuthenticationManager
) : ViewModel() {

    val email = TextFieldState()
    val password = TextFieldState()
    val isValidEmail by derivedStateOf {
        isValidEmail(email.text.toString())
    }
    private val _showPassword = MutableStateFlow(false)
    val showPassword = _showPassword.asStateFlow()


    private fun isValidEmail(email: String): Boolean {
       return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun onShowPasswordClicked() {
        _showPassword.update {
            !it
        }
    }

    fun onLoginButtonClicked() {
        viewModelScope.launch {
            manager.logIn(
                email = email.text.toString(),
                password = password.text.toString()
            )
        }
    }
}