package com.rkm.tasky.network.authentication.implementation

import com.rkm.tasky.di.IoDispatcher
import com.rkm.tasky.network.authentication.abstraction.AuthenticationManager
import com.rkm.tasky.network.model.dto.asAuthInfo
import com.rkm.tasky.network.repository.abstraction.AuthenticationRepository
import com.rkm.tasky.network.util.NetworkError
import com.rkm.tasky.util.result.EmptyResult
import com.rkm.tasky.util.result.Result
import com.rkm.tasky.util.result.asEmptyDataResult
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
    ): EmptyResult<NetworkError.APIError> =
        withContext(dispatcher) {
            val result = repository.loginUser(email = email, password = password)
            result.onSuccess { user ->
                sessionStorage.setSession(user.asAuthInfo())
            }
            return@withContext result.asEmptyDataResult()
        }

    override suspend fun logOut(): EmptyResult<NetworkError.APIError> = withContext(dispatcher) {
        val result = repository.logoutUser()
        result.onSuccess {
            sessionStorage.clearSession()
        }
        return@withContext result.asEmptyDataResult()
    }

    override suspend fun checkAuthentication(): EmptyResult<NetworkError.APIError> =
        withContext(dispatcher) {
            return@withContext checkAuthentication().asEmptyDataResult()
        }

    override suspend fun getNewAccessToken(): EmptyResult<NetworkError.APIError> =
        withContext(dispatcher) {
            val authInfo = sessionStorage.getSession() ?: return@withContext Result.Error(
                NetworkError.APIError.UNKNOWN
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

            return@withContext result.asEmptyDataResult()
        }

    override suspend fun registerUser(
        fullName: String,
        email: String,
        password: String
    ): EmptyResult<NetworkError.APIError> = withContext(dispatcher) {
        return@withContext repository.registerUser(
            fullName = fullName,
            email = email,
            password = password
        ).asEmptyDataResult()
    }
}