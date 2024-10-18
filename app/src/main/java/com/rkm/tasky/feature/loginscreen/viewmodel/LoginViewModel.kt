package com.rkm.tasky.feature.loginscreen.viewmodel

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    val email = TextFieldState()
    val password = TextFieldState()
//    private val _isValidEmail = MutableStateFlow(isValidEmail(email.text.toString()))
    private val _isValidEmail = MutableStateFlow(false)
    val isValidEmail = _isValidEmail.asStateFlow()
    private val _showPassword = MutableStateFlow(false)
    val showPassword = _showPassword.asStateFlow()


    private fun isValidEmail(email: String): Boolean {
       TODO("Not implemented yet")
    }

    fun onShowPasswordClicked() {
        _showPassword.update {
            !it
        }
    }
}