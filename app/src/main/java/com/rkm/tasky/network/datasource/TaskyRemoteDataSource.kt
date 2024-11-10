package com.rkm.tasky.network.datasource

import RequestType
import com.rkm.tasky.network.model.Reminder
import com.rkm.tasky.network.model.Task
import com.rkm.tasky.network.model.request.CreateEventRequest
import com.rkm.tasky.network.model.request.SyncAgendaRequest
import com.rkm.tasky.network.model.request.UpdateEventRequest
import com.rkm.tasky.network.model.response.AgendaResponse
import com.rkm.tasky.network.model.response.AttendeeResponse
import com.rkm.tasky.network.model.response.EventResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query
import retrofit2.http.Tag

interface TaskyRemoteDataSource {

    @GET("/agenda")
    suspend fun getAgenda(
        @Query("time") time: Long,
        @Tag authorization: RequestType = RequestType.AUTHORIZATION
    ): Response<AgendaResponse>

    @POST("/syncAgenda")
    suspend fun syncAgenda(
        @Body agenda: SyncAgendaRequest,
        @Tag authorization: RequestType = RequestType.AUTHORIZATION
    ): Response<Unit>

    @POST("/fullAgenda")
    suspend fun getFullAgenda(
        @Tag authorization: RequestType = RequestType.AUTHORIZATION
    ): Response<AgendaResponse>

    @Multipart
    @POST("/event")
    suspend fun createEvent(
        @Part("create_event_request") createEventRequest: CreateEventRequest,
        @Part photos: List<MultipartBody.Part>,
        @Tag authorization: RequestType = RequestType.AUTHORIZATION
    ): Response<EventResponse>

    @GET("/event")
    suspend fun getEvent(
        @Query("eventId") eventId: String,
        @Tag authorization: RequestType = RequestType.AUTHORIZATION
    ): Response<EventResponse>

    @DELETE("/event")
    suspend fun deleteEvent(
        @Query("eventId") eventId: String,
        @Tag authorization: RequestType = RequestType.AUTHORIZATION
    ): Response<Unit>

    @Multipart
    @PUT("/event")
    suspend fun updateEvent(
        @Part("update_event_request") updateEventRequest: UpdateEventRequest,
        @Part photos: List<MultipartBody.Part>,
        @Tag authorization: RequestType = RequestType.AUTHORIZATION
    ): Response<Unit>

    @GET
    suspend fun getAttendee(
        @Query("email") email: String,
        @Tag authorization: RequestType = RequestType.AUTHORIZATION
    ): Response<AttendeeResponse>

    @DELETE
    suspend fun deleteAttendee(
        @Query("eventId") eventId: String,
        @Tag authorization: RequestType = RequestType.AUTHORIZATION
    ): Response<Unit>

    @GET("/task")
    suspend fun getTask(
        @Query("taskId") taskId: String,
        @Tag authorization: RequestType = RequestType.AUTHORIZATION
    ): Response<Task>

    @DELETE("/task")
    suspend fun deleteTask(
        @Query("taskId") taskId: String,
        @Tag authorization: RequestType = RequestType.AUTHORIZATION
    ): Response<Unit>

    @PUT("/task")
    suspend fun updateTask(
        @Body taskRequest: Task,
        @Tag authorization: RequestType = RequestType.AUTHORIZATION
    ): Response<Unit>

    @POST("/task")
    suspend fun createTask(
        @Body taskRequest: Task,
        @Tag authorization: RequestType = RequestType.AUTHORIZATION
    ): Response<Unit>

}