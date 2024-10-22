package com.rkm.tasky.network.authentication.abstraction

import com.rkm.tasky.network.util.NetworkError
import com.rkm.tasky.util.result.EmptyResult

interface AuthenticationManager {
    suspend fun logIn(email: String, password: String): EmptyResult<NetworkError.APIError>
    suspend fun logOut(): EmptyResult<NetworkError.APIError>
    suspend fun checkAuthentication(): EmptyResult<NetworkError.APIError>
    suspend fun getNewAccessToken(): EmptyResult<NetworkError.APIError>
    suspend fun registerUser(fullName: String, email: String, password: String): EmptyResult<NetworkError.APIError>
}