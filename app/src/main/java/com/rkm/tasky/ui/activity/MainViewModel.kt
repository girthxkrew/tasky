package com.rkm.tasky.ui.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rkm.tasky.network.authorization.abstraction.AuthorizationManager
import com.rkm.tasky.util.result.onFailure
import com.rkm.tasky.util.result.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val manager: AuthorizationManager
): ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState = _authState.asStateFlow()

    init {
        checkAuthentication()
    }

    private fun checkAuthentication() {
        viewModelScope.launch {

            val authInfo = manager.getSessionInfo()
            if(authInfo == null) {
                _authState.update { AuthState.NoSessionInfo }
                return@launch
            }
            val results = manager.checkAuthentication()
            results.onSuccess { _authState.update { AuthState.AuthenticatedUser } }
            results.onFailure { _authState.update { AuthState.Error } }
        }
    }
}

sealed interface AuthState {
    data object Loading: AuthState
    data object NoSessionInfo: AuthState
    data object AuthenticatedUser: AuthState
    data object Error: AuthState
}
