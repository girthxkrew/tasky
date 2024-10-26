package com.rkm.tasky.network.authorization.abstraction

import com.rkm.tasky.network.authorization.model.AuthInfoDTO
import com.rkm.tasky.network.util.NetworkError
import com.rkm.tasky.util.result.EmptyResult

interface AuthorizationManager {
    suspend fun logOut(): EmptyResult<NetworkError.APIError>
    suspend fun checkAuthentication(): EmptyResult<NetworkError.APIError>
    suspend fun getNewAccessToken(): EmptyResult<NetworkError.APIError>
    suspend fun getSessionInfo(): AuthInfoDTO?
}