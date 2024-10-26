package com.rkm.tasky.network.authorization.implementation

import com.rkm.tasky.di.IoDispatcher
import com.rkm.tasky.network.authorization.abstraction.AuthorizationManager
import com.rkm.tasky.network.authorization.model.AuthInfoDTO
import com.rkm.tasky.network.repository.abstraction.AuthorizationRepository
import com.rkm.tasky.network.util.NetworkError
import com.rkm.tasky.util.result.EmptyResult
import com.rkm.tasky.util.result.Result
import com.rkm.tasky.util.result.asEmptyDataResult
import com.rkm.tasky.util.result.onSuccess
import com.rkm.tasky.util.storage.abstraction.SessionStorage
import com.rkm.tasky.util.storage.model.asAuthInfoDTO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthorizationManagerImpl @Inject constructor(
    private val repository: AuthorizationRepository,
    private val sessionStorage: SessionStorage,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
): AuthorizationManager {
    override suspend fun logOut(): EmptyResult<NetworkError.APIError> = withContext(dispatcher) {
        val result = repository.logoutUser()
        result.onSuccess {
            sessionStorage.clearSession()
        }
        return@withContext result.asEmptyDataResult()
    }

    override suspend fun checkAuthentication(): EmptyResult<NetworkError.APIError> =
        withContext(dispatcher) {
            return@withContext repository.checkAuthentication().asEmptyDataResult()
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

    override suspend fun getSessionInfo(): AuthInfoDTO? = withContext(dispatcher){
        val authInfo = sessionStorage.getSession()?.asAuthInfoDTO()
        return@withContext authInfo
    }
}