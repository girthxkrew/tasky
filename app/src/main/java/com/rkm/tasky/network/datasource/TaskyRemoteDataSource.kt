package com.rkm.tasky.network.datasource

import com.rkm.tasky.network.Reminder
import com.rkm.tasky.network.Task
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
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query

internal interface TaskyRemoteDataSource {

    @POST("/register")
    suspend fun registerUser(
        @Body request: RegisterUserRequest
    ): Response<Unit>

    @POST("/login")
    suspend fun loginUser(
        @Body request: LoginUserRequest
    ): Response<LoginUserResponse>

    @POST("/accessToken")
    suspend fun getNewAccessToken(
        @Body request: AccessTokenRequest
    ): Response<AccessTokenResponse>

    @GET("/authenticate")
    suspend fun checkAuthentication(): Response<Unit>

    @GET("/logout")
    suspend fun logoutUser(): Response<Unit>

    @GET("/agenda")
    suspend fun getAgenda(
        @Query("time") time: Long
    ): Response<AgendaResponse>

    @POST("/syncAgenda")
    suspend fun syncAgenda(
        @Body agenda: SyncAgendaRequest
    ): Response<Unit>

    @POST("/fullAgenda")
    suspend fun getFullAgenda(): Response<AgendaResponse>

    @Multipart
    @POST("/event")
    suspend fun createEvent(
        @Part("create_event_request") createEventRequest: CreateEventRequest,
        @Part photos: List<MultipartBody.Part>
    ): Response<EventResponse>

    @GET("/event")
    suspend fun getEvent(
        @Query("eventId"
        ) eventId: String): Response<EventResponse>

    @DELETE("/event")
    suspend fun deleteEvent(
        @Query("eventId") eventId: String
    ): Response<Unit>

    @Multipart
    @PUT("/event")
    suspend fun updateEvent(
        @Part("update_event_request") updateEventRequest: UpdateEventRequest,
        @Part photos: List<MultipartBody.Part>
    )

    @GET
    suspend fun getAttendee(
        @Query("email") email: String
    ): Response<AttendeeResponse>

    @DELETE
    suspend fun deleteAttendee(
        @Query("eventId") eventId: String
    ): Response<Unit>

    @GET("/task")
    suspend fun getTask(
        @Query("taskId") taskId: String
    ): Response<Task>

    @DELETE("/task")
    suspend fun deleteTask(
        @Query("taskId") taskId: String
    ): Response<Unit>

    @PUT("/task")
    suspend fun updateTask(
        @Body taskRequest: Task
    ): Response<Unit>

    @POST("/task")
    suspend fun createTask(
        @Body taskRequest: Task
    ): Response<Unit>

    @POST("/reminder")
    suspend fun createReminder(
        @Body reminderRequest: Reminder
    )

    @PUT("/reminder")
    suspend fun updateReminder(
        @Body reminderRequest: Reminder
    ): Response<Unit>

    @GET("/reminder")
    suspend fun getReminder(
        @Query("reminderId") reminderId: String
    ): Response<Reminder>

    @DELETE("/reminder")
    suspend fun deleteReminder(
        @Query("reminderId") reminderId: String
    ): Response<Unit>


}