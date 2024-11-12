package com.rkm.tasky.network.repository.implementation

import com.rkm.tasky.di.IoDispatcher
import com.rkm.tasky.network.datasource.TaskyAuthorizationRemoteDateSource
import com.rkm.tasky.network.model.request.AccessTokenRequest
import com.rkm.tasky.network.model.response.AccessTokenDTO
import com.rkm.tasky.network.repository.abstraction.AuthorizationRepository
import com.rkm.tasky.network.util.NetworkError
import com.rkm.tasky.network.util.safeCall
import com.rkm.tasky.util.result.EmptyResult
import com.rkm.tasky.util.result.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthorizationRepositoryImpl @Inject constructor(
    private val dataSource: TaskyAuthorizationRemoteDateSource,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
): AuthorizationRepository {
    override suspend fun getNewAccessToken(
        refreshToken: String,
        userId: String
    ): Result<AccessTokenDTO, NetworkError.APIError> = withContext(dispatcher) {
        val result = safeCall {
            dataSource.getNewAccessToken(
                AccessTokenRequest(
                    refreshToken = refreshToken,
                    userId = userId
                )
            )
        }
        return@withContext when (result) {
            is Result.Success -> Result.Success(result.data)
            is Result.Error -> result
        }
    }

    override suspend fun checkAuthentication(): EmptyResult<NetworkError.APIError> =
        withContext(dispatcher) {
            return@withContext safeCall { dataSource.checkAuthentication() }
        }

    override suspend fun logoutUser(): EmptyResult<NetworkError.APIError> =
        withContext(dispatcher) {
            return@withContext safeCall { dataSource.logoutUser() }
        }
}