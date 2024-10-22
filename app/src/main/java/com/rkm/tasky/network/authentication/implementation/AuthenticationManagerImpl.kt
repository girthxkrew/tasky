package com.rkm.tasky.network.authentication.implementation

import com.rkm.tasky.di.IoDispatcher
import com.rkm.tasky.network.authentication.abstraction.AuthError
import com.rkm.tasky.network.authentication.abstraction.AuthenticationManager
import com.rkm.tasky.network.model.dto.asAuthInfo
import com.rkm.tasky.network.repository.abstraction.AuthenticationRepository
import com.rkm.tasky.network.util.NetworkError
import com.rkm.tasky.util.result.EmptyResult
import com.rkm.tasky.util.result.Error
import com.rkm.tasky.util.result.Result
import com.rkm.tasky.util.result.asEmptyDataResult
import com.rkm.tasky.util.result.onFailure
import com.rkm.tasky.util.result.onSuccess
import com.rkm.tasky.util.storage.abstraction.SessionStorage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthenticationManagerImpl @Inject constructor(
    private val repository: AuthenticationRepository,
    private val sessionStorage: SessionStorage,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : AuthenticationManager {
    override suspend fun logIn(
        email: String,
        password: String
    ): EmptyResult<AuthError> =
        withContext(dispatcher) {
            val result = repository.loginUser(email = email, password = password)
            result.onSuccess { user ->
                sessionStorage.setSession(user.asAuthInfo())
            }
            return@withContext handleResults(result, AuthError.LOGIN_FAILED)
        }

    override suspend fun logOut(): EmptyResult<AuthError> = withContext(dispatcher) {
        val result = repository.logoutUser()
        result.onSuccess {
            sessionStorage.clearSession()
        }
        return@withContext handleResults(result, AuthError.LOGOUT_FAILED)
    }

    override suspend fun checkAuthentication(): EmptyResult<AuthError> =
        withContext(dispatcher) {
            return@withContext handleResults(
                repository.checkAuthentication(),
                AuthError.AUTHENTICATION_CHECK_FAILED
            )
        }

    override suspend fun getNewAccessToken(): EmptyResult<AuthError> = withContext(dispatcher) {
        val authInfo = sessionStorage.getSession() ?: return@withContext Result.Error(
            AuthError.NO_USER_SESSION_IN_DATASTORE
        )

        val result = repository.getNewAccessToken(
            refreshToken = authInfo.refreshToken,
            userId = authInfo.userId
        )

        result.onSuccess { newAccessToken ->
            sessionStorage.setSession(
                authInfo.copy(
                    accessToken = newAccessToken.accessToken,
                    accessTokenExpirationTimestamp = newAccessToken.expirationTimestamp
                )
            )
        }

        return@withContext handleResults(result, AuthError.GET_NEW_ACCESS_TOKEN_FAILED)
    }

    override suspend fun registerUser(
        fullName: String,
        email: String,
        password: String
    ): EmptyResult<AuthError> = withContext(dispatcher) {
        val result =
            repository.registerUser(fullName = fullName, email = email, password = password)
        return@withContext handleResults(result, AuthError.REGISTRATION_FAILED)
    }

    private fun handleResults(
        result: Result<Any, Error>,
        error: AuthError
    ): EmptyResult<AuthError> {
        return when (result) {
            is Result.Success -> result.asEmptyDataResult()
            is Result.Error -> {
                if (result.error == NetworkError.APIError.NO_INTERNET) {
                    Result.Error(AuthError.NO_INTERNET)
                } else {
                    Result.Error(error)
                }
            }
        }
    }
}