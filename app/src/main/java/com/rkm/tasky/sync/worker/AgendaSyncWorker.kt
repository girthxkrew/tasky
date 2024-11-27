package com.rkm.tasky.sync.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.rkm.tasky.database.TaskyDatabase
import com.rkm.tasky.database.dao.AgendaDao
import com.rkm.tasky.database.model.AgendaDetails
import com.rkm.tasky.database.model.AttendeeEntity
import com.rkm.tasky.database.model.EventAttendeesAndPhotosToDelete
import com.rkm.tasky.database.model.PhotoEntity
import com.rkm.tasky.di.ApplicationCoroutineScope
import com.rkm.tasky.di.IoDispatcher
import com.rkm.tasky.network.datasource.TaskyAgendaRemoteDataSource
import com.rkm.tasky.network.util.NetworkError
import com.rkm.tasky.network.util.safeCall
import com.rkm.tasky.repository.mapper.asEventWithDetails
import com.rkm.tasky.repository.mapper.asReminderEntity
import com.rkm.tasky.repository.mapper.asTaskEntity
import com.rkm.tasky.util.result.Result
import com.rkm.tasky.util.result.onFailure
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext

@HiltWorker
class AgendaSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val agendaRemoteDataSource: TaskyAgendaRemoteDataSource,
    private val agendaLocalDataSource: AgendaDao,
    private val db: TaskyDatabase,
    @ApplicationCoroutineScope private val scope: CoroutineScope,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
): CoroutineWorker(context, params) {
    override suspend fun doWork(): Result  = withContext(scope.coroutineContext + dispatcher){
        val agendaResults = safeCall { agendaRemoteDataSource.getFullAgenda() }

        agendaResults.onFailure { error ->
            when(error) {
                NetworkError.APIError.NO_INTERNET -> {
                    return@withContext Result.retry()
                }
                else -> {
                    return@withContext Result.failure()
                }
            }
        }

        val result = (agendaResults as com.rkm.tasky.util.result.Result.Success).data

        val remoteEvents = result.events.map { it.asEventWithDetails() }
        val remoteReminders = result.reminders.map { it.asReminderEntity() }
        val remoteTasks = result.tasks.map { it.asTaskEntity() }

        val localAgenda = agendaLocalDataSource.getAllAgendaItems()

        val reminderToDelete = localAgenda.reminders.filter { localReminder ->
            remoteReminders.none { remoteReminder -> remoteReminder.id == localReminder.id }
        }

        val tasksToDelete = localAgenda.tasks.filter { localTask ->
            remoteTasks.none { remoteTask -> remoteTask.id == localTask.id }
        }

        val eventToDelete = localAgenda.events.filter { localEvent ->
            remoteEvents.none { remoteEvent -> remoteEvent.event.id == localEvent.event.id }
        }

        val eventsMap = remoteEvents.associateBy { it.event.id }
        val attendeesToDelete = mutableListOf<AttendeeEntity>()
        val photosToDelete = mutableListOf<PhotoEntity>()

        localAgenda.events.forEach { oldEvent ->
            if(eventsMap.containsKey(oldEvent.event.id)) {
                val newEvent = eventsMap[oldEvent.event.id]!!

                attendeesToDelete += oldEvent.attendees.filter { oldAttendee ->
                    newEvent.attendees.none { newAttendee -> newAttendee.userId == oldAttendee.userId }
                }

                photosToDelete += oldEvent.photos.filter { oldPhoto ->
                    newEvent.photos.none { newPhoto -> newPhoto.key == oldPhoto.key }
                }
            }
        }

        val agendaItemsToDelete = AgendaDetails(
            events = eventToDelete,
            reminders = reminderToDelete,
            tasks = tasksToDelete
        )

        val photosAndAttendeesToDelete = EventAttendeesAndPhotosToDelete(
            attendees = attendeesToDelete,
            photos = photosToDelete
        )

        val remoteAgenda = AgendaDetails(remoteEvents, remoteReminders, remoteTasks)

        agendaLocalDataSource.syncAgenda(
            db = db,
            toDelete = agendaItemsToDelete,
            agendaDetails = remoteAgenda,
            deleteDetails = photosAndAttendeesToDelete
        )

        return@withContext Result.success()
    }
}