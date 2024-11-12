package com.rkm.tasky.network.authentication.implementation

import com.rkm.tasky.di.IoDispatcher
import com.rkm.tasky.network.authentication.abstraction.AuthenticationManager
import com.rkm.tasky.network.authentication.mapper.asSessionInfo
import com.rkm.tasky.network.repository.abstraction.AuthenticationRepository
import com.rkm.tasky.network.util.NetworkError
import com.rkm.tasky.util.result.EmptyResult
import com.rkm.tasky.util.result.asEmptyDataResult
import com.rkm.tasky.util.result.onSuccess
import com.rkm.tasky.util.storage.abstraction.SessionStorage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthenticationManagerImpl @Inject constructor(
    private val repository: AuthenticationRepository,
    private val sessionStorage: SessionStorage,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : AuthenticationManager {
    override suspend fun logIn(
        email: String,
        password: String
    ): EmptyResult<NetworkError.APIError> =
        withContext(dispatcher) {
            val result = repository.loginUser(email = email, password = password)
            result.onSuccess { user ->
                sessionStorage.setSession(user.asSessionInfo())
            }
            return@withContext result.asEmptyDataResult()
        }

    override suspend fun registerUser(
        fullName: String,
        email: String,
        password: String
    ): EmptyResult<NetworkError.APIError> = withContext(dispatcher) {
        return@withContext repository.registerUser(
            fullName = fullName,
            email = email,
            password = password
        ).asEmptyDataResult()
    }
}