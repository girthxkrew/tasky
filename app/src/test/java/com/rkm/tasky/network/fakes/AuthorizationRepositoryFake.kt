package com.rkm.tasky.network.fakes

import com.rkm.tasky.network.model.dto.AccessTokenDTO
import com.rkm.tasky.network.model.response.asAccessTokenDTO
import com.rkm.tasky.network.repository.abstraction.AuthorizationRepository
import com.rkm.tasky.network.util.NetworkError
import com.rkm.tasky.resources.response.getNewAccessTokenResponseToPojo
import com.rkm.tasky.util.result.EmptyResult
import com.rkm.tasky.util.result.Result
import com.rkm.tasky.util.result.asEmptyDataResult

class AuthorizationRepositoryFake: AuthorizationRepository {

    private var hasError: Boolean = false

    override suspend fun getNewAccessToken(
        refreshToken: String,
        userId: String
    ): Result<AccessTokenDTO, NetworkError.APIError> {
        return if(!hasError) Result.Success(getNewAccessTokenResponseToPojo().asAccessTokenDTO())
        else Result.Error(NetworkError.APIError.UNAUTHORIZED)
    }

    override suspend fun checkAuthentication(): EmptyResult<NetworkError.APIError> {
        return handleResults()
    }

    override suspend fun logoutUser(): EmptyResult<NetworkError.APIError> {
        return handleResults()
    }

    fun setHasError(hasError: Boolean) {
        this.hasError = hasError
    }

    private fun handleResults(): EmptyResult<NetworkError.APIError> {
        return if(!hasError) Result.Success(Unit).asEmptyDataResult()
        else Result.Error(NetworkError.APIError.UNAUTHORIZED)
    }
}