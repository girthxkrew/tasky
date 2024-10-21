package com.rkm.tasky.network.repository.implementation

import com.rkm.tasky.di.IoDispatcher
import com.rkm.tasky.network.datasource.TaskyRemoteDataSource
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
import com.rkm.tasky.network.repository.abstraction.TaskyRemoteRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class TaskyRemoteRepositoryImpl @Inject constructor(
    private val dataSource: TaskyRemoteDataSource,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
): TaskyRemoteRepository {
    override suspend fun registerUser(request: RegisterUserRequest): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun loginUser(
        request: LoginUserRequest
    ): Result<LoginUserResponse> = withContext(dispatcher) {
        return@withContext try {
            val response = dataSource.loginUser(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else if (response.body() == null) {
                Result.failure(NullPointerException())
            } else {
                Result.failure(HttpException(response))
            }
        } catch (e: IOException) {
            Result.failure(e)
        }
    }

    override suspend fun getNewAccessToken(request: AccessTokenRequest): Result<AccessTokenResponse> = withContext(dispatcher) {
        return@withContext try {
            val response = dataSource.getNewAccessToken(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else if (response.body() == null) {
                Result.failure(NullPointerException())
            } else {
                Result.failure(HttpException(response))
            }
        } catch (e: IOException) {
            Result.failure(e)
        }
    }

    override suspend fun checkAuthentication(): Result<Unit> = withContext(dispatcher){
        return@withContext try {
            val response = dataSource.checkAuthentication()
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(HttpException(response))
            }
        } catch (e: IOException) {
            Result.failure(e)
        }
    }

    override suspend fun logoutUser(): Result<Unit> = withContext(dispatcher){
        return@withContext try {
            val response = dataSource.logoutUser()
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(HttpException(response))
            }
        } catch (e: IOException) {
            Result.failure(e)
        }
    }

    override suspend fun getAgenda(time: Long): Result<AgendaResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun syncAgenda(agenda: SyncAgendaRequest): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun getFullAgenda(): Result<AgendaResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun createEvent(
        createEventRequest: CreateEventRequest,
        photos: List<ByteArray>
    ): Result<EventResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getEvent(eventId: String): Result<EventResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteEvent(eventId: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun updateEvent(
        updateEventRequest: UpdateEventRequest,
        photos: List<ByteArray>
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun getAttendee(email: String): Result<AttendeeResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAttendee(eventId: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun getTask(taskId: String): Result<Task> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTask(taskId: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun updateTask(taskRequest: Task): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun createTask(taskRequest: Task): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun createReminder(reminderRequest: Reminder): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun updateReminder(reminderRequest: Reminder): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun getReminder(reminderId: String): Result<Reminder> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteReminder(reminderId: String): Result<Unit> {
        TODO("Not yet implemented")
    }
}