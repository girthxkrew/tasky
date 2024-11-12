package com.rkm.tasky.repository.abstraction

import com.rkm.tasky.feature.task.model.Task
import com.rkm.tasky.network.util.NetworkError
import com.rkm.tasky.util.result.EmptyResult
import com.rkm.tasky.util.result.Result

interface TaskyTaskRepository {
    suspend fun getTask(id: String): Result<Task?, NetworkError.APIError>
    suspend fun createTask(task: Task): EmptyResult<NetworkError.APIError>
    suspend fun updateTask(task: Task): EmptyResult<NetworkError.APIError>
    suspend fun deleteTask(task: Task): EmptyResult<NetworkError.APIError>
}