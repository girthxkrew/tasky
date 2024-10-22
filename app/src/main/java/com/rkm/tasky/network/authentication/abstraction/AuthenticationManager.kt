package com.rkm.tasky.network.authentication.abstraction

import com.rkm.tasky.network.util.NetworkError
import com.rkm.tasky.util.result.EmptyResult

interface AuthenticationManager {
    suspend fun logIn(email: String, password: String): EmptyResult<NetworkError.AuthError>
    suspend fun logOut(): EmptyResult<NetworkError.AuthError>
    suspend fun checkAuthentication(): EmptyResult<NetworkError.AuthError>
    suspend fun getNewAccessToken(): EmptyResult<NetworkError.AuthError>
    suspend fun registerUser(fullName: String, email: String, password: String): EmptyResult<NetworkError.AuthError>
}