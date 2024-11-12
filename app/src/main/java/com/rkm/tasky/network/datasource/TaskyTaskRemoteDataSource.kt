package com.rkm.tasky.network.datasource

import RequestType
import com.rkm.tasky.network.model.request.TaskRequest
import com.rkm.tasky.network.model.response.TaskDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query
import retrofit2.http.Tag

interface TaskyTaskRemoteDataSource {

    @GET("/task")
    suspend fun getTask(
        @Query("taskId") taskId: String,
        @Tag authorization: RequestType = RequestType.AUTHORIZATION
    ): Response<TaskDTO>

    @DELETE("/task")
    suspend fun deleteTask(
        @Query("taskId") taskId: String,
        @Tag authorization: RequestType = RequestType.AUTHORIZATION
    ): Response<Unit>

    @PUT("/task")
    suspend fun updateTask(
        @Body taskRemoteRequest: TaskRequest,
        @Tag authorization: RequestType = RequestType.AUTHORIZATION
    ): Response<Unit>

    @POST("/task")
    suspend fun createTask(
        @Body taskRemoteRequest: TaskRequest,
        @Tag authorization: RequestType = RequestType.AUTHORIZATION
    ): Response<Unit>

}