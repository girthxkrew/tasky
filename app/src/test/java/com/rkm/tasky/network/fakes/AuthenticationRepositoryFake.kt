package com.rkm.tasky.network.fakes

import com.rkm.tasky.network.model.response.LoginUserDTO
import com.rkm.tasky.network.repository.abstraction.AuthenticationRepository
import com.rkm.tasky.network.util.NetworkError
import com.rkm.tasky.resources.response.loginUserResponseToPojo
import com.rkm.tasky.util.result.EmptyResult
import com.rkm.tasky.util.result.Result
import com.rkm.tasky.util.result.asEmptyDataResult

class AuthenticationRepositoryFake: AuthenticationRepository {

    private var hasError: Boolean = false

    override suspend fun registerUser(
        fullName: String,
        email: String,
        password: String
    ): EmptyResult<NetworkError.APIError> {
        return handleResults()
    }

    override suspend fun loginUser(
        email: String,
        password: String
    ): Result<LoginUserDTO, NetworkError.APIError> {
        return if(!hasError) Result.Success(loginUserResponseToPojo())
        else Result.Error(NetworkError.APIError.UNAUTHORIZED)
    }

    fun setHasError(hasError: Boolean) {
        this.hasError = hasError
    }

    private fun handleResults(): EmptyResult<NetworkError.APIError> {
        return if(!hasError) Result.Success(Unit).asEmptyDataResult()
        else Result.Error(NetworkError.APIError.UNAUTHORIZED)
    }
}