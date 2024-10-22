package com.rkm.tasky.network.repository.implementation

import com.rkm.tasky.di.IoDispatcher
import com.rkm.tasky.network.datasource.TaskyRemoteDataSource
import com.rkm.tasky.network.model.dto.AccessTokenDTO
import com.rkm.tasky.network.model.dto.LoginDTO
import com.rkm.tasky.network.model.request.AccessTokenRequest
import com.rkm.tasky.network.model.request.LoginUserRequest
import com.rkm.tasky.network.model.request.RegisterUserRequest
import com.rkm.tasky.network.model.response.asAccessTokenDTO
import com.rkm.tasky.network.model.response.asLoginDTO
import com.rkm.tasky.network.repository.abstraction.AuthenticationRepository
import com.rkm.tasky.network.util.NetworkError
import com.rkm.tasky.util.result.EmptyResult
import com.rkm.tasky.util.result.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val dataSource: TaskyRemoteDataSource,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
): AuthenticationRepository {
    override suspend fun registerUser(
        fullName: String,
        email: String,
        password: String
    ): EmptyResult<NetworkError.AuthError> = withContext(dispatcher) {
        return@withContext try {
            val response = dataSource.registerUser(RegisterUserRequest(fullName, email, password))
            if (response.isSuccessful) {
               Result.Success(Unit)
            } else {
                Result.Error(NetworkError.AuthError.REGISTER_USER_ERROR)
            }
        } catch(exception: IOException) {
            exception.asAuthError()
        }
    }

    override suspend fun loginUser(
        email: String,
        password: String
    ): Result<LoginDTO, NetworkError.AuthError> = withContext(dispatcher) {
        return@withContext try {
            val response = dataSource.loginUser(LoginUserRequest(email, password))
            if (response.isSuccessful) {
                Result.Success(response.body()!!.asLoginDTO())
            } else {
                Result.Error(NetworkError.AuthError.LOGIN_USER_ERROR)
            }
        } catch(exception: IOException) {
            exception.asAuthError()
        }
    }

    override suspend fun getNewAccessToken(
        refreshToken: String,
        userId: String
    ): Result<AccessTokenDTO, NetworkError.AuthError> = withContext(dispatcher) {
        return@withContext try {
            val response = dataSource.getNewAccessToken(AccessTokenRequest(refreshToken, userId))
            if (response.isSuccessful) {
                Result.Success(response.body()!!.asAccessTokenDTO())
            } else {
                Result.Error(NetworkError.AuthError.INVALID_TOKEN)
            }
        } catch(exception: IOException) {
            exception.asAuthError()
        }
    }

    override suspend fun checkAuthentication(): EmptyResult<NetworkError.AuthError> = withContext(dispatcher) {
        return@withContext try {
            val response = dataSource.checkAuthentication()
            if (response.isSuccessful) {
                Result.Success(Unit)
            } else {
                Result.Error(NetworkError.AuthError.CHECK_AUTHENTICATION_ERROR)
            }
        } catch (exception: IOException) {
            exception.asAuthError()
        }
    }
    override suspend fun logoutUser(): EmptyResult<NetworkError.AuthError> = withContext(dispatcher) {
        return@withContext try {
            val response = dataSource.logoutUser()
            if (response.isSuccessful) {
                Result.Success(Unit)
            } else {
                Result.Error(NetworkError.AuthError.LOGOUT_ERROR)
            }
        } catch (exception: IOException) {
            exception.asAuthError()
        }
    }
}

private fun Exception.asAuthError(): Result.Error<NetworkError.AuthError> {
    return when(this) {
        is IOException -> Result.Error(NetworkError.AuthError.NO_INTERNET)
        else -> {Result.Error(NetworkError.AuthError.UNKNOWN)}
    }
}