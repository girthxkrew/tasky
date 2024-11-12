package com.rkm.tasky.network.fakes

import com.rkm.tasky.network.authentication.abstraction.AuthenticationManager
import com.rkm.tasky.network.authentication.mapper.asSessionInfo
import com.rkm.tasky.network.util.NetworkError
import com.rkm.tasky.util.result.EmptyResult
import com.rkm.tasky.util.result.asEmptyDataResult
import com.rkm.tasky.util.result.onSuccess

class AuthenticationManagerFake(
    private val repository: AuthenticationRepositoryFake,
    private val storage: SessionStorageFake
): AuthenticationManager {

    override suspend fun logIn(
        email: String,
        password: String
    ): EmptyResult<NetworkError.APIError> {
        val result = repository.loginUser(email, password)
        result.onSuccess {
            storage.setSession(it.asSessionInfo())
        }
        return result.asEmptyDataResult()
    }

    override suspend fun registerUser(
        fullName: String,
        email: String,
        password: String
    ): EmptyResult<NetworkError.APIError> {
        return repository.registerUser(fullName, email, password).asEmptyDataResult()
    }
}