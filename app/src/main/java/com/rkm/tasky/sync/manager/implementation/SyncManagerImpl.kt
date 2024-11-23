package com.rkm.tasky.sync.manager.implementation

import com.rkm.tasky.database.dao.ReminderDao
import com.rkm.tasky.database.dao.SyncDao
import com.rkm.tasky.database.dao.TaskDao
import com.rkm.tasky.database.model.SyncItemType
import com.rkm.tasky.database.model.SyncUserAction
import com.rkm.tasky.di.IoDispatcher
import com.rkm.tasky.network.datasource.TaskyAgendaRemoteDataSource
import com.rkm.tasky.network.datasource.TaskyEventRemoteDataSource
import com.rkm.tasky.network.datasource.TaskyReminderRemoteDataSource
import com.rkm.tasky.network.datasource.TaskyTaskRemoteDataSource
import com.rkm.tasky.network.util.NetworkError
import com.rkm.tasky.network.util.safeCall
import com.rkm.tasky.sync.manager.abstraction.SyncManager
import com.rkm.tasky.sync.mapper.asReminderRequest
import com.rkm.tasky.sync.mapper.asSyncAgendaRequest
import com.rkm.tasky.sync.mapper.asTaskRequest
import com.rkm.tasky.sync.repository.abstraction.SyncCreateEventRepository
import com.rkm.tasky.sync.repository.abstraction.SyncUpdateEventRepository
import com.rkm.tasky.util.result.EmptyResult
import com.rkm.tasky.util.result.Result
import com.rkm.tasky.util.result.asEmptyDataResult
import com.rkm.tasky.util.result.onSuccess
import kotlinx.coroutines.CoroutineDispatcher
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
    @IoDispatcher private val dispatcher: CoroutineDispatcher
): SyncManager {
    override suspend fun syncDeletedItems(): EmptyResult<NetworkError.APIError> = withContext(dispatcher) {
        val syncItems = syncDataSource.getSyncItemsByAction(SyncUserAction.DELETE.name)
        if(syncItems.isNullOrEmpty()) {
            return@withContext Result.Success(syncItems).asEmptyDataResult()
        }
        val networkResult = safeCall { agendaRemoteDataSource.syncAgenda(syncItems.asSyncAgendaRequest()) }

        networkResult.onSuccess {
            syncDataSource.deleteSyncItems(syncItems.map { item -> item.itemId})
        }

        return@withContext networkResult.asEmptyDataResult()
    }

    override suspend fun syncUpdatedItem(): EmptyResult<NetworkError.APIError> = withContext(dispatcher) {
        val syncItems = syncDataSource.getSyncItemsByAction(SyncUserAction.UPDATE.name)
        if(syncItems.isNullOrEmpty()) {
            return@withContext Result.Success(syncItems).asEmptyDataResult()
        }
        val taskIds = syncItems.filter { it.item == SyncItemType.TASK }.map { it.itemId }
        val reminderIds = syncItems.filter { it.item == SyncItemType.REMINDER }.map { it.itemId }
        val eventsIds = syncItems.filter { it.item == SyncItemType.EVENT }.map { it.itemId }

        val taskToSync = taskLocalDataSource.getTasksById(taskIds)
        val reminderToSync = reminderLocalDataSource.getRemindersById(reminderIds)
        val eventsToSync = updateEventRepository.getEvents(eventsIds)

        val itemsToRemoveList = mutableListOf<String>()
        taskToSync?.forEach { task ->
            val result = safeCall { taskRemoteDataSource.updateTask(task.asTaskRequest()) }
            result.onSuccess { itemsToRemoveList.add(task.id) }
        }
        reminderToSync?.forEach { reminder ->
            val result = safeCall { reminderRemoteDataSource.updateReminder(reminder.asReminderRequest()) }
            result.onSuccess { itemsToRemoveList.add(reminder.id) }
        }
        val clearEventList = mutableListOf<String>()
        eventsToSync.onSuccess { events ->
            events.forEach {
                //Implement logic to convert photos
                //val result = eventRemoteDataSource.updateEvent(it.asUpdateEventRequest(), )
                //result.onSuccess {clearEventList.add(it.event.id)}
            }
        }
        syncDataSource.deleteSyncItems(itemsToRemoveList + clearEventList)
        updateEventRepository.deleteEvents(clearEventList)
        return@withContext Result.Success(taskToSync).asEmptyDataResult()
    }

    override suspend fun syncCreatedItems(): EmptyResult<NetworkError.APIError> = withContext(dispatcher){
        val syncItems = syncDataSource.getSyncItemsByAction(SyncUserAction.CREATE.name)
        if(syncItems.isNullOrEmpty()) {
            return@withContext Result.Success(syncItems).asEmptyDataResult()
        }
        val taskIds = syncItems.filter { it.item == SyncItemType.TASK }.map { it.itemId }
        val reminderIds = syncItems.filter { it.item == SyncItemType.REMINDER }.map { it.itemId }
        val eventsIds = syncItems.filter { it.item == SyncItemType.EVENT }.map { it.itemId }

        val taskToSync = taskLocalDataSource.getTasksById(taskIds)
        val reminderToSync = reminderLocalDataSource.getRemindersById(reminderIds)
        val eventsToSync = createEventRepository.getEvents(eventsIds)

        val itemsToRemoveList = mutableListOf<String>()
        taskToSync?.forEach { task ->
            val result = safeCall { taskRemoteDataSource.createTask(task.asTaskRequest()) }
            result.onSuccess { itemsToRemoveList.add(task.id) }
        }
        reminderToSync?.forEach { reminder ->
            val result = safeCall { reminderRemoteDataSource.createReminder(reminder.asReminderRequest()) }
            result.onSuccess { itemsToRemoveList.add(reminder.id) }
        }
        val clearEventList = mutableListOf<String>()
        eventsToSync.onSuccess { events ->
            events.forEach {
                //Implement logic to convert photos
                //val result = eventRemoteDataSource.createEvent(it.asCreateEventRequest(), )
                //result.onSuccess {clearEventList.add(it.event.id)}
            }
        }
        syncDataSource.deleteSyncItems(itemsToRemoveList + clearEventList)
        createEventRepository.deleteEvents(clearEventList)
        return@withContext Result.Success(taskToSync).asEmptyDataResult()
    }
}