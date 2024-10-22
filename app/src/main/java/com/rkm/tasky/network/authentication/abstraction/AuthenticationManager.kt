package com.rkm.tasky.network.authentication.abstraction

import com.rkm.tasky.network.util.NetworkError
import com.rkm.tasky.util.result.EmptyResult
import com.rkm.tasky.util.result.Error

interface AuthenticationManager {
    suspend fun logIn(email: String, password: String): EmptyResult<AuthError>
    suspend fun logOut(): EmptyResult<AuthError>
    suspend fun checkAuthentication(): EmptyResult<AuthError>
    suspend fun getNewAccessToken(): EmptyResult<AuthError>
    suspend fun registerUser(fullName: String, email: String, password: String): EmptyResult<AuthError>
}

enum class AuthError: Error {
    NO_INTERNET,
    LOGIN_FAILED,
    LOGOUT_FAILED,
    AUTHENTICATION_CHECK_FAILED,
    NO_USER_SESSION_IN_DATASTORE,
    GET_NEW_ACCESS_TOKEN_FAILED,
    REGISTRATION_FAILED
}