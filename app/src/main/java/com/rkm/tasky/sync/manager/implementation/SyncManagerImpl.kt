package com.rkm.tasky.sync.manager.implementation

import com.rkm.tasky.database.dao.AttendeeDao
import com.rkm.tasky.database.dao.EventDao
import com.rkm.tasky.database.dao.PhotoDao
import com.rkm.tasky.database.dao.ReminderDao
import com.rkm.tasky.database.dao.SyncDao
import com.rkm.tasky.database.dao.TaskDao
import com.rkm.tasky.database.model.SyncItemType
import com.rkm.tasky.database.model.SyncUserAction
import com.rkm.tasky.di.IoDispatcher
import com.rkm.tasky.feature.event.model.Event
import com.rkm.tasky.network.datasource.TaskyAgendaRemoteDataSource
import com.rkm.tasky.network.datasource.TaskyEventRemoteDataSource
import com.rkm.tasky.network.datasource.TaskyReminderRemoteDataSource
import com.rkm.tasky.network.datasource.TaskyTaskRemoteDataSource
import com.rkm.tasky.network.util.NetworkError
import com.rkm.tasky.network.util.asMultiPartBody
import com.rkm.tasky.network.util.safeCall
import com.rkm.tasky.repository.mapper.asAttendeeEntity
import com.rkm.tasky.repository.mapper.asEventEntity
import com.rkm.tasky.repository.mapper.asPhotoEntity
import com.rkm.tasky.sync.manager.abstraction.SyncManager
import com.rkm.tasky.sync.mapper.asCreateEventRequest
import com.rkm.tasky.sync.mapper.asReminderRequest
import com.rkm.tasky.sync.mapper.asSyncAgendaRequest
import com.rkm.tasky.sync.mapper.asTaskRequest
import com.rkm.tasky.sync.mapper.asUpdateEventRequest
import com.rkm.tasky.sync.repository.abstraction.SyncCreateEventRepository
import com.rkm.tasky.sync.repository.abstraction.SyncUpdateEventRepository
import com.rkm.tasky.util.image.ImageProcessor
import com.rkm.tasky.util.result.EmptyResult
import com.rkm.tasky.util.result.Result
import com.rkm.tasky.util.result.asEmptyDataResult
import com.rkm.tasky.util.result.onSuccess
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SyncManagerImpl @Inject constructor(
    private val syncDataSource: SyncDao,
    private val agendaRemoteDataSource: TaskyAgendaRemoteDataSource,
    private val eventRemoteDataSource: TaskyEventRemoteDataSource,
    private val reminderRemoteDataSource: TaskyReminderRemoteDataSource,
    private val taskRemoteDataSource: TaskyTaskRemoteDataSource,
    private val createEventRepository: SyncCreateEventRepository,
    private val updateEventRepository: SyncUpdateEventRepository,
    private val taskLocalDataSource: TaskDao,
    private val reminderLocalDataSource: ReminderDao,
    private val eventLocalDataSource: EventDao,
    private val attendeeLocalDataSource: AttendeeDao,
    private val photoLocalDataSource: PhotoDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val imageProcessor: ImageProcessor
): SyncManager {
    override suspend fun syncDeletedItems(): EmptyResult<NetworkError.APIError> = withContext(dispatcher) {
        val syncItems = syncDataSource.getSyncItemsByAction(SyncUserAction.DELETE.name)
        if(syncItems.isEmpty()) {
            return@withContext Result.Success(Unit).asEmptyDataResult()
        }
        val networkResult = safeCall { agendaRemoteDataSource.syncAgenda(syncItems.asSyncAgendaRequest()) }

        networkResult.onSuccess {
            syncDataSource.deleteSyncItems(syncItems.map { item -> item.itemId})
        }

        return@withContext networkResult.asEmptyDataResult()
    }

    override suspend fun syncUpdatedItems(): EmptyResult<NetworkError.APIError> = withContext(dispatcher) {
        val syncItems = syncDataSource.getSyncItemsByAction(SyncUserAction.UPDATE.name)
        val numOfItems = syncItems.size
        var totalCalls = 0
        if(syncItems.isEmpty()) {
            return@withContext Result.Success(Unit).asEmptyDataResult()
        }
        val taskIds = syncItems.filter { it.item == SyncItemType.TASK }.map { it.itemId }
        val reminderIds = syncItems.filter { it.item == SyncItemType.REMINDER }.map { it.itemId }
        val eventsIds = syncItems.filter { it.item == SyncItemType.EVENT }.map { it.itemId }

        val taskToSync = async { taskLocalDataSource.getTasksById(taskIds) }
        val reminderToSync = async { reminderLocalDataSource.getRemindersById(reminderIds) }
        val eventsToSync = async { updateEventRepository.getEvents(eventsIds) }

        val itemsToRemoveList = mutableListOf<String>()
        taskToSync.await().map { task ->
            launch {
                val result = safeCall { taskRemoteDataSource.updateTask(task.asTaskRequest()) }
                result.onSuccess {
                    itemsToRemoveList.add(task.id)
                    totalCalls++
                }
            }
        }.joinAll()
        reminderToSync.await().map { reminder ->
            launch {
                val result = safeCall { reminderRemoteDataSource.updateReminder(reminder.asReminderRequest()) }
                result.onSuccess {
                    itemsToRemoveList.add(reminder.id)
                    totalCalls++
                }
            }
        }.joinAll()
        val clearEventList = mutableListOf<String>()
        eventsToSync.await().onSuccess { events ->
            events.map { event ->
                launch {
                    val photosToUpload = event.photos.mapNotNull { imageProcessor.getImageFromUri(it.filePath) }.asMultiPartBody()
                    val result = safeCall { eventRemoteDataSource.updateEvent(event.asUpdateEventRequest(), photosToUpload) }
                    result.onSuccess {
                        clearEventList.add(event.event.id)
                        totalCalls++
                        eventLocalDataSource.upsertAllEventInfo(
                            event = it.asEventEntity(),
                            attendees = it.attendees.map { attendee -> attendee.asAttendeeEntity() },
                            photos = it.photos.map { photo -> photo.asPhotoEntity(it.id) },
                            photoDao = photoLocalDataSource,
                            attendeeDao = attendeeLocalDataSource
                        )
                    }
                }
            }.joinAll()
        }

        listOf(
            launch { syncDataSource.deleteSyncItems(itemsToRemoveList + clearEventList) },
            launch { updateEventRepository.deleteEvents(clearEventList) },
        ).joinAll()

        return@withContext when {
            totalCalls == numOfItems -> Result.Success(Unit).asEmptyDataResult()
            totalCalls == 0 -> Result.Error(NetworkError.APIError.UPLOAD_FAILED)
            totalCalls < numOfItems -> Result.Error(NetworkError.APIError.RETRY)
            else -> Result.Error(NetworkError.APIError.UNKNOWN)
        }
    }

    override suspend fun syncCreatedItems(): EmptyResult<NetworkError.APIError> = withContext(dispatcher){
        val syncItems = syncDataSource.getSyncItemsByAction(SyncUserAction.CREATE.name)
        val numOfItems = syncItems.size
        var totalCalls = 0
        if(syncItems.isEmpty()) {
            return@withContext Result.Success(Unit).asEmptyDataResult()
        }
        val taskIds = syncItems.filter { it.item == SyncItemType.TASK }.map { it.itemId }
        val reminderIds = syncItems.filter { it.item == SyncItemType.REMINDER }.map { it.itemId }
        val eventsIds = syncItems.filter { it.item == SyncItemType.EVENT }.map { it.itemId }

        val taskToSync = async { taskLocalDataSource.getTasksById(taskIds) }
        val reminderToSync = async { reminderLocalDataSource.getRemindersById(reminderIds) }
        val eventsToSync = async { createEventRepository.getEvents(eventsIds) }

        val itemsToRemoveList = mutableListOf<String>()
        taskToSync.await().map { task ->
            launch {
                val result = safeCall { taskRemoteDataSource.createTask(task.asTaskRequest()) }
                result.onSuccess {
                    itemsToRemoveList.add(task.id)
                    totalCalls++
                }
            }
        }.joinAll()
        reminderToSync.await().map { reminder ->
            launch {
                val result = safeCall { reminderRemoteDataSource.createReminder(reminder.asReminderRequest()) }
                result.onSuccess {
                    itemsToRemoveList.add(reminder.id)
                    totalCalls++
                }
            }
        }.joinAll()
        val clearEventList = mutableListOf<String>()
        eventsToSync.await().onSuccess { events ->
            events.map { event ->
                launch {
                    val photosToUpload = event.photos.mapNotNull { imageProcessor.getImageFromUri(it.filePath) }.asMultiPartBody()
                    val result = safeCall { eventRemoteDataSource.createEvent(event.asCreateEventRequest(), photosToUpload) }
                    result.onSuccess {
                        clearEventList.add(event.event.id)
                        eventLocalDataSource.upsertAllEventInfo(
                            event = it.asEventEntity(),
                            attendees = it.attendees.map { attendee -> attendee.asAttendeeEntity() },
                            photos = it.photos.map { photo -> photo.asPhotoEntity(it.id) },
                            photoDao = photoLocalDataSource,
                            attendeeDao = attendeeLocalDataSource
                        )
                        totalCalls++
                    }
                }
            }.joinAll()
        }

        listOf(
            launch { syncDataSource.deleteSyncItems(itemsToRemoveList + clearEventList) },
            launch { createEventRepository.deleteEvents(clearEventList) }
        ).joinAll()

        return@withContext when {
            totalCalls == numOfItems -> Result.Success(Unit).asEmptyDataResult()
            totalCalls == 0 -> Result.Error(NetworkError.APIError.UPLOAD_FAILED)
            totalCalls < numOfItems -> Result.Error(NetworkError.APIError.RETRY)
            else -> Result.Error(NetworkError.APIError.UNKNOWN)
        }
    }
}