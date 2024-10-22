package com.rkm.tasky.network.repository.abstraction

import com.rkm.tasky.network.model.Reminder
import com.rkm.tasky.network.model.Task
import com.rkm.tasky.network.model.request.AccessTokenRequest
import com.rkm.tasky.network.model.request.CreateEventRequest
import com.rkm.tasky.network.model.request.LoginUserRequest
import com.rkm.tasky.network.model.request.RegisterUserRequest
import com.rkm.tasky.network.model.request.SyncAgendaRequest
import com.rkm.tasky.network.model.request.UpdateEventRequest
import com.rkm.tasky.network.model.response.AccessTokenResponse
import com.rkm.tasky.network.model.response.AgendaResponse
import com.rkm.tasky.network.model.response.AttendeeResponse
import com.rkm.tasky.network.model.response.EventResponse
import com.rkm.tasky.network.model.response.LoginUserResponse

interface TaskyRemoteRepository {
    suspend fun registerUser(request: RegisterUserRequest): Result<Unit>
    suspend fun loginUser(request: LoginUserRequest): Result<LoginUserResponse>
    suspend fun getNewAccessToken(request: AccessTokenRequest): Result<AccessTokenResponse>
    suspend fun checkAuthentication(): Result<Unit>
    suspend fun logoutUser(): Result<Unit>
    suspend fun getAgenda(time: Long): Result<AgendaResponse>
    suspend fun syncAgenda(agenda: SyncAgendaRequest): Result<Unit>
    suspend fun getFullAgenda(): Result<AgendaResponse>
    suspend fun createEvent(
        createEventRequest: CreateEventRequest,
        photos: List<ByteArray>
    ): Result<EventResponse>
    suspend fun getEvent(eventId: String): Result<EventResponse>
    suspend fun deleteEvent(eventId: String): Result<Unit>
    suspend fun updateEvent(
       updateEventRequest: UpdateEventRequest,
       photos: List<ByteArray>
    )
    suspend fun getAttendee(email: String): Result<AttendeeResponse>
    suspend fun deleteAttendee(eventId: String): Result<Unit>
    suspend fun getTask(taskId: String): Result<Task>
    suspend fun deleteTask(taskId: String): Result<Unit>
    suspend fun updateTask(taskRequest: Task): Result<Unit>
    suspend fun createTask(taskRequest: Task): Result<Unit>
    suspend fun createReminder(reminderRequest: Reminder): Result<Unit>
    suspend fun updateReminder(reminderRequest: Reminder): Result<Unit>
    suspend fun getReminder(reminderId: String): Result<Reminder>
    suspend fun deleteReminder(reminderId: String): Result<Unit>
}