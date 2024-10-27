package com.rkm.tasky.network.datasource

import RequestType
import com.rkm.tasky.network.model.request.AccessTokenRequest
import com.rkm.tasky.network.model.response.AccessTokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Tag

interface TaskyAuthorizationRemoteDateSource {

    @POST("/accessToken")
    suspend fun getNewAccessToken(
        @Body request: AccessTokenRequest,
        @Tag authorization: RequestType = RequestType.AUTHORIZATION
    ): Response<AccessTokenResponse>

    @GET("/authenticate")
    suspend fun checkAuthentication(
        @Tag authorization: RequestType = RequestType.AUTHORIZATION
    ): Response<Unit>

    @GET("/logout")
    suspend fun logoutUser(
        @Tag authorization: RequestType = RequestType.AUTHORIZATION
    ): Response<Unit>

}