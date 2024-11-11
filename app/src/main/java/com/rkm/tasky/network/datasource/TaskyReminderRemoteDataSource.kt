package com.rkm.tasky.network.datasource

import ReminderRequest
import RequestType
import com.rkm.tasky.network.model.response.ReminderDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query
import retrofit2.http.Tag

interface TaskyReminderRemoteDataSource {
    @POST("/reminder")
    suspend fun createReminder(
        @Body reminderRemoteRequest: ReminderRequest,
        @Tag authorization: RequestType = RequestType.AUTHORIZATION
    ): Response<Unit>

    @PUT("/reminder")
    suspend fun updateReminder(
        @Body reminderRemoteRequest: ReminderRequest,
        @Tag authorization: RequestType = RequestType.AUTHORIZATION
    ): Response<Unit>

    @GET("/reminder")
    suspend fun getReminder(
        @Query("reminderId") reminderId: String,
        @Tag authorization: RequestType = RequestType.AUTHORIZATION
    ): Response<ReminderDTO>

    @DELETE("/reminder")
    suspend fun deleteReminder(
        @Query("reminderId") reminderId: String,
        @Tag authorization: RequestType = RequestType.AUTHORIZATION
    ): Response<Unit>
}