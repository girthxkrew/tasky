package com.rkm.tasky.feature.loginscreen.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel: ViewModel() {

    private val _emailState = MutableStateFlow("")
    val emailState = _emailState.asStateFlow()

    private val _isValidEmailState = MutableStateFlow(false)
    val isValidEmailState = _isValidEmailState.asStateFlow()

    private val _passwordState = MutableStateFlow("")
    val passwordState = _passwordState.asStateFlow()

    private val _showPasswordState = MutableStateFlow(false)
    val showPasswordState = _isValidEmailState.asStateFlow()

    fun updateEmail(email: String) {
        _emailState.update {
            it + email
        }

    }

    fun updatePassword(password: String) {
        _passwordState.update {
            it + password
        }

    }


}