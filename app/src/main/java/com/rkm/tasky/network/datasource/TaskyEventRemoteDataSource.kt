package com.rkm.tasky.network.datasource

import RequestType
import com.rkm.tasky.network.model.request.CreateEventRequest
import com.rkm.tasky.network.model.request.UpdateEventRequest
import com.rkm.tasky.network.model.response.EventDTO
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query
import retrofit2.http.Tag

interface TaskyEventRemoteDataSource {

    @Multipart
    @POST("/event")
    suspend fun createEvent(
        @Part("create_event_request") createEventRequest: CreateEventRequest,
        @Part photos: List<MultipartBody.Part>,
        @Tag authorization: RequestType = RequestType.AUTHORIZATION
    ): Response<EventDTO>

    @GET("/event")
    suspend fun getEvent(
        @Query("eventId") eventId: String,
        @Tag authorization: RequestType = RequestType.AUTHORIZATION
    ): Response<EventDTO>

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

}