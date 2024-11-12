package com.rkm.tasky.repository.implementation

import com.rkm.tasky.database.dao.SyncDao
import com.rkm.tasky.database.dao.TaskDao
import com.rkm.tasky.database.error.DatabaseError
import com.rkm.tasky.database.model.SyncEntity
import com.rkm.tasky.database.model.SyncItemType
import com.rkm.tasky.database.model.SyncUserAction
import com.rkm.tasky.di.IoDispatcher
import com.rkm.tasky.feature.task.model.Task
import com.rkm.tasky.network.datasource.TaskyTaskRemoteDataSource
import com.rkm.tasky.network.util.NetworkError
import com.rkm.tasky.network.util.safeCall
import com.rkm.tasky.repository.abstraction.TaskyTaskRepository
import com.rkm.tasky.repository.mapper.asTask
import com.rkm.tasky.repository.mapper.asTaskEntity
import com.rkm.tasky.repository.mapper.asTaskyRequest
import com.rkm.tasky.util.result.EmptyResult
import com.rkm.tasky.util.result.Result
import com.rkm.tasky.util.result.onFailure
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TaskyTaskRepositoryImpl @Inject constructor(
    private val remoteDataSource: TaskyTaskRemoteDataSource,
    private val localDataSource: TaskDao,
    private val syncDataSource: SyncDao,
    @IoDispatcher private val  dispatcher: CoroutineDispatcher
): TaskyTaskRepository {
    override suspend fun getTask(id: String): Result<Task?, DatabaseError.ItemError> = withContext(dispatcher){
       return@withContext when(val result = localDataSource.getTaskById(id)) {
           null -> Result.Error(DatabaseError.ItemError.ITEM_DOES_NOT_EXIST)
           else -> Result.Success(result.asTask())
       }
    }

    override suspend fun createTask(task: Task): EmptyResult<NetworkError.APIError> {
        val results = safeCall { remoteDataSource.createTask(task.asTaskyRequest()) }
        results.onFailure {
            syncDataSource.upsertSyncItem(
                SyncEntity(
                    action = SyncUserAction.CREATE,
                    item = SyncItemType.TASK,
                    itemId = task.id
                )
            )
        }
        localDataSource.upsertTask(task.asTaskEntity())
        return results
    }

    override suspend fun updateTask(task: Task): EmptyResult<NetworkError.APIError> {
        val results = safeCall { remoteDataSource.updateTask(task.asTaskyRequest()) }
        results.onFailure {
            syncDataSource.upsertSyncItem(
                SyncEntity(
                    action = SyncUserAction.UPDATE,
                    item = SyncItemType.TASK,
                    itemId = task.id
                )
            )
        }
        localDataSource.upsertTask(task.asTaskEntity())
        return results
    }

    override suspend fun deleteTask(task: Task): EmptyResult<NetworkError.APIError> {
        val results = safeCall { remoteDataSource.deleteTask(task.id) }
        results.onFailure {
            syncDataSource.upsertSyncItem(
                SyncEntity(
                    action = SyncUserAction.DELETE,
                    item = SyncItemType.TASK,
                    itemId = task.id
                )
            )
        }
        localDataSource.deleteTask(task.asTaskEntity())
        return results
    }
}