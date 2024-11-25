package com.rkm.tasky.network.repository.implementation

import com.rkm.tasky.di.IoDispatcher
import com.rkm.tasky.network.datasource.TaskyAuthenticationRemoteDataSource
import com.rkm.tasky.network.model.request.LoginUserRequest
import com.rkm.tasky.network.model.request.RegisterUserRequest
import com.rkm.tasky.network.model.response.LoginUserDTO
import com.rkm.tasky.network.repository.abstraction.AuthenticationRepository
import com.rkm.tasky.network.util.NetworkError
import com.rkm.tasky.network.util.safeCall
import com.rkm.tasky.util.result.EmptyResult
import com.rkm.tasky.util.result.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val dataSource: TaskyAuthenticationRemoteDataSource,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : AuthenticationRepository {
    override suspend fun registerUser(
        fullName: String,
        email: String,
        password: String
    ): EmptyResult<NetworkError.APIError> = withContext(dispatcher) {
        return@withContext safeCall {
            dataSource.registerUser(
                RegisterUserRequest(
                    fullName = fullName,
                    email = email,
                    password = password
                )
            )
        }
    }

    override suspend fun loginUser(
        email: String,
        password: String
    ): Result<LoginUserDTO, NetworkError.APIError> = withContext(dispatcher) {
        val result =
            safeCall { dataSource.loginUser(LoginUserRequest(email = email, password = password)) }
        return@withContext when (result) {
            is Result.Success -> Result.Success(result.data)
            is Result.Error -> result
        }
    }
}