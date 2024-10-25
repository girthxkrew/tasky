package com.rkm.tasky.network.repository.abstraction

import com.rkm.tasky.network.model.dto.AccessTokenDTO
import com.rkm.tasky.network.util.NetworkError
import com.rkm.tasky.util.result.EmptyResult
import com.rkm.tasky.util.result.Result

interface AuthorizationRepository {
    suspend fun getNewAccessToken(
        refreshToken: String,
        userId: String
    ): Result<AccessTokenDTO, NetworkError.APIError>
    suspend fun checkAuthentication(): EmptyResult<NetworkError.APIError>
    suspend fun logoutUser(): EmptyResult<NetworkError.APIError>
}