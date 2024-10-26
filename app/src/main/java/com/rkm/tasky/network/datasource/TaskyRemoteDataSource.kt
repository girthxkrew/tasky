package com.rkm.tasky.network.datasource

import com.rkm.tasky.network.model.Reminder
import com.rkm.tasky.network.model.Task
import com.rkm.tasky.network.model.request.AccessTokenRequest
import com.rkm.tasky.network.model.request.CreateEventRequest
import com.rkm.tasky.network.model.response.LoginUserResponse
import com.rkm.tasky.network.model.request.LoginUserRequest
import com.rkm.tasky.network.model.request.RegisterUserRequest
import com.rkm.tasky.network.model.request.SyncAgendaRequest
import com.rkm.tasky.network.model.request.UpdateEventRequest
import com.rkm.tasky.network.model.response.AccessTokenResponse
import com.rkm.tasky.network.model.response.AgendaResponse
import com.rkm.tasky.network.model.response.AttendeeResponse
import com.rkm.tasky.network.model.response.EventResponse
import okhttp3.MultipartBody
import okhttp3.Request
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

    @POST("/register")
    suspend fun registerUser(
        @Body request: RegisterUserRequest,
        @Tag authorization: RequestType = RequestType.AUTHENTICATION
    ): Response<Unit>

    @POST("/login")
    suspend fun loginUser(
        @Body request: LoginUserRequest,
        @Tag authorization: RequestType = RequestType.AUTHENTICATION
    ): Response<LoginUserResponse>

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

    @POST("/reminder")
    suspend fun createReminder(
        @Body reminderRequest: Reminder,
        @Tag authorization: RequestType = RequestType.AUTHORIZATION
    )

    @PUT("/reminder")
    suspend fun updateReminder(
        @Body reminderRequest: Reminder,
        @Tag authorization: RequestType = RequestType.AUTHORIZATION
    ): Response<Unit>

    @GET("/reminder")
    suspend fun getReminder(
        @Query("reminderId") reminderId: String,
        @Tag authorization: RequestType = RequestType.AUTHORIZATION
    ): Response<Reminder>

    @DELETE("/reminder")
    suspend fun deleteReminder(
        @Query("reminderId") reminderId: String,
        @Tag authorization: RequestType = RequestType.AUTHORIZATION
    ): Response<Unit>

    enum class RequestType {
        AUTHENTICATION,
        AUTHORIZATION;

        companion object {
            fun fromRequest(request: Request): RequestType =
                request.tag(RequestType::class.java) ?: AUTHENTICATION
        }
    }
}