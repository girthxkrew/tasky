package com.rkm.tasky.network.authentication.implementation

import com.rkm.tasky.di.IoDispatcher
import com.rkm.tasky.network.authentication.abstraction.LoginManager
import com.rkm.tasky.network.model.request.AccessTokenRequest
import com.rkm.tasky.network.model.request.LoginUserRequest
import com.rkm.tasky.network.model.response.asAuthInfo
import com.rkm.tasky.network.repository.abstraction.TaskyRemoteRepository
import com.rkm.tasky.util.storage.implementation.SessionStorageImpl
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoginManagerImpl @Inject constructor(
    private val repository: TaskyRemoteRepository,
    private val sessionStorage: SessionStorageImpl,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : LoginManager {
    override suspend fun logIn(email: String, password: String): Result<Boolean> =
        withContext(dispatcher) {
            val result = repository.loginUser(LoginUserRequest(email, password))
            return@withContext if (result.isSuccess) {
                sessionStorage.setSession(result.getOrNull()!!.asAuthInfo())
                Result.success(true)
            } else {
                Result.failure(result.exceptionOrNull()!!)
            }
        }

    override suspend fun logOut(): Result<Boolean> = withContext(dispatcher) {
        val result = repository.logoutUser()
        return@withContext if (result.isSuccess) {
            sessionStorage.clearSession()
            Result.success(true)
        } else {
            Result.failure(result.exceptionOrNull()!!)
        }
    }

    override suspend fun checkAuthentication(): Result<Boolean> = withContext(dispatcher) {
        val result = repository.checkAuthentication()
        return@withContext if (result.isSuccess) {
            Result.success(true)
        } else {
            Result.failure(result.exceptionOrNull()!!)
        }
    }

    override suspend fun getNewAccessToken(): Result<Unit> = withContext(dispatcher) {
        val authInfo = sessionStorage.getSession()
        val result = repository.getNewAccessToken(
            AccessTokenRequest(
                authInfo!!.refreshToken,
                authInfo.userId
            )
        )
        return@withContext if (result.isSuccess) {
            sessionStorage.setSession(
                authInfo.copy(
                    accessToken = result.getOrNull()!!.accessToken,
                    accessTokenExpirationTimestamp = result.getOrNull()!!.expirationTimestamp
                )
            )
            Result.success(Unit)
        } else {
            Result.failure(result.exceptionOrNull()!!)
        }
    }
}