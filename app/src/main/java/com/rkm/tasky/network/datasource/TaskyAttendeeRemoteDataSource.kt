package com.rkm.tasky.network.datasource

import RequestType
import com.rkm.tasky.network.model.response.AttendeeDTO
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Tag

interface TaskyAttendeeRemoteDataSource {

    @GET
    suspend fun getAttendee(
        @Query("email") email: String,
        @Tag authorization: RequestType = RequestType.AUTHORIZATION
    ): Response<AttendeeDTO>

    @DELETE
    suspend fun deleteAttendee(
        @Query("eventId") eventId: String,
        @Tag authorization: RequestType = RequestType.AUTHORIZATION
    ): Response<Unit>

}