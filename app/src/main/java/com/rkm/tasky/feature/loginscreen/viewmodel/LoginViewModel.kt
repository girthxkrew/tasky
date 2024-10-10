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

    fun updateEmail(email: String) {
        _emailState.update {
            it + email
        }

    }


}