package com.rkm.tasky.network.repository.abstraction


import com.rkm.tasky.network.model.response.LoginUserDTO
import com.rkm.tasky.network.util.NetworkError
import com.rkm.tasky.util.result.EmptyResult
import com.rkm.tasky.util.result.Result

interface AuthenticationRepository {
    suspend fun registerUser(
        fullName: String,
        email: String,
        password: String
    ): EmptyResult<NetworkError.APIError>
    suspend fun loginUser(
        email: String,
        password: String
    ): Result<LoginUserDTO, NetworkError.APIError>
}