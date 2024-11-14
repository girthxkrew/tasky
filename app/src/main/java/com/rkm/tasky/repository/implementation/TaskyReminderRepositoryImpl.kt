package com.rkm.tasky.repository.implementation

import com.rkm.tasky.database.dao.ReminderDao
import com.rkm.tasky.database.dao.SyncDao
import com.rkm.tasky.database.error.DatabaseError
import com.rkm.tasky.database.model.SyncEntity
import com.rkm.tasky.database.model.SyncItemType
import com.rkm.tasky.database.model.SyncUserAction
import com.rkm.tasky.di.IoDispatcher
import com.rkm.tasky.network.datasource.TaskyReminderRemoteDataSource
import com.rkm.tasky.network.util.NetworkError
import com.rkm.tasky.network.util.safeCall
import com.rkm.tasky.repository.abstraction.TaskyReminderRepository
import com.rkm.tasky.repository.mapper.asReminder
import com.rkm.tasky.repository.mapper.asReminderEntity
import com.rkm.tasky.repository.mapper.asReminderRequest
import com.rkm.tasky.feature.reminder.model.Reminder
import com.rkm.tasky.util.result.EmptyResult
import com.rkm.tasky.util.result.Result
import com.rkm.tasky.util.result.onFailure
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TaskyReminderRepositoryImpl @Inject constructor(
    private val remoteDataSource: TaskyReminderRemoteDataSource,
    private val localDataSource: ReminderDao,
    private val syncDataSource: SyncDao,
    @IoDispatcher private val  dispatcher: CoroutineDispatcher
): TaskyReminderRepository {
    override suspend fun getReminder(id: String): Result<Reminder, DatabaseError.ItemError> = withContext(dispatcher) {
        return@withContext when(val result = localDataSource.getReminderById(id)) {
            null -> Result.Error(DatabaseError.ItemError.ITEM_DOES_NOT_EXIST)
            else -> Result.Success(result.asReminder())
        }
    }

    override suspend fun createReminder(reminder: Reminder): EmptyResult<NetworkError.APIError> = withContext(dispatcher) {
        val result = safeCall { remoteDataSource.createReminder(reminder.asReminderRequest()) }
        result.onFailure {
            syncDataSource.upsertSyncItem(
                SyncEntity(
                    action = SyncUserAction.CREATE,
                    item = SyncItemType.REMINDER,
                    itemId = reminder.id
                )
            )
        }
        localDataSource.upsertReminder(reminder.asReminderEntity())
        return@withContext result
    }

    override suspend fun updateReminder(reminder: Reminder): EmptyResult<NetworkError.APIError> = withContext(dispatcher) {
        val result = safeCall { remoteDataSource.updateReminder(reminder.asReminderRequest()) }
        result.onFailure {
            syncDataSource.upsertSyncItem(
                SyncEntity(
                    action = SyncUserAction.UPDATE,
                    item = SyncItemType.REMINDER,
                    itemId = reminder.id
                )
            )
        }
        localDataSource.upsertReminder(reminder.asReminderEntity())
        return@withContext result
    }

    override suspend fun deleteReminder(reminder: Reminder): EmptyResult<NetworkError.APIError>  = withContext(dispatcher) {
        val result = safeCall { remoteDataSource.deleteReminder(reminder.id) }
        result.onFailure {
            syncDataSource.upsertSyncItem(
                SyncEntity(
                    action = SyncUserAction.DELETE,
                    item = SyncItemType.REMINDER,
                    itemId = reminder.id
                )
            )
        }
        localDataSource.deleteReminder(reminder.asReminderEntity())
        return@withContext result
    }
}