package com.rkm.tasky.network.datasource

import RequestType
import com.rkm.tasky.network.model.request.CreateEventRequest
import com.rkm.tasky.network.model.request.SyncAgendaRequest
import com.rkm.tasky.network.model.request.UpdateEventRequest
import com.rkm.tasky.network.model.response.AgendaDTO
import com.rkm.tasky.network.model.response.AttendeeDTO
import com.rkm.tasky.network.model.response.EventDTO
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
    ): Response<AgendaDTO>

    @POST("/syncAgenda")
    suspend fun syncAgenda(
        @Body agenda: SyncAgendaRequest,
        @Tag authorization: RequestType = RequestType.AUTHORIZATION
    ): Response<Unit>

    @POST("/fullAgenda")
    suspend fun getFullAgenda(
        @Tag authorization: RequestType = RequestType.AUTHORIZATION
    ): Response<AgendaDTO>

}