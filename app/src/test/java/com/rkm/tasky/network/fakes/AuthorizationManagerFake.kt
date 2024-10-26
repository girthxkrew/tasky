package com.rkm.tasky.network.fakes

import com.rkm.tasky.network.authorization.abstraction.AuthorizationManager
import com.rkm.tasky.network.authorization.model.AuthInfoDTO
import com.rkm.tasky.network.util.NetworkError
import com.rkm.tasky.util.result.EmptyResult
import com.rkm.tasky.util.result.Result
import com.rkm.tasky.util.result.asEmptyDataResult
import com.rkm.tasky.util.result.onSuccess
import com.rkm.tasky.util.storage.model.asAuthInfoDTO

class AuthorizationManagerFake(
    private val repository: AuthorizationRepositoryFake,
    private val storage: SessionStorageFake
): AuthorizationManager {
    override suspend fun logOut(): EmptyResult<NetworkError.APIError> {
        return repository.logoutUser().asEmptyDataResult()
    }

    override suspend fun checkAuthentication(): EmptyResult<NetworkError.APIError> {
        return repository.checkAuthentication().asEmptyDataResult()
    }

    override suspend fun getNewAccessToken(): EmptyResult<NetworkError.APIError> {
        val authInfo = storage.getSession() ?: return Result.Error(
            NetworkError.APIError.UNKNOWN
        )

        val result = repository.getNewAccessToken(
            refreshToken = authInfo.refreshToken,
            userId = authInfo.userId
        )

        result.onSuccess { newAccessToken ->
            storage.setSession(
                authInfo.copy(
                    accessToken = newAccessToken.accessToken,
                    accessTokenExpirationTimestamp = newAccessToken.expirationTimestamp
                )
            )
        }

        return result.asEmptyDataResult()
    }

    override suspend fun getSessionInfo(): AuthInfoDTO? {
        val result = storage.getSession()?.asAuthInfoDTO()
        return result
    }
}