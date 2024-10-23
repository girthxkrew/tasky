package com.rkm.tasky.network.authentication.abstraction

interface LoginManager {
    suspend fun logIn(email: String, password: String): Result<Boolean>
    suspend fun logOut(): Result<Boolean>
    suspend fun checkAuthentication(): Result<Boolean>
    suspend fun getNewAccessToken(): Result<Unit>
}