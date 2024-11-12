package com.rkm.tasky.network.datasource

import RequestType
import com.rkm.tasky.network.model.request.LoginUserRequest
import com.rkm.tasky.network.model.request.RegisterUserRequest
import com.rkm.tasky.network.model.response.LoginUserDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Tag

interface TaskyAuthenticationRemoteDataSource {

    @POST("/register")
    suspend fun registerUser(
        @Body request: RegisterUserRequest,
        @Tag authorization: RequestType = RequestType.AUTHENTICATION
    ): Response<Unit>

    @POST("/login")
    suspend fun loginUser(
        @Body request: LoginUserRequest,
        @Tag authorization: RequestType = RequestType.AUTHENTICATION
    ): Response<LoginUserDTO>

}