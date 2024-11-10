package com.rkm.tasky.repository.abstraction

import com.rkm.tasky.network.util.NetworkError
import com.rkm.tasky.repository.model.Reminder
import com.rkm.tasky.util.result.EmptyResult
import com.rkm.tasky.util.result.Result

interface TaskyReminderRepository {
    suspend fun getReminder(id: String): Result<Reminder?, NetworkError.APIError>
    suspend fun createReminder(reminder: Reminder): EmptyResult<NetworkError.APIError>
    suspend fun updateReminder(reminder: Reminder): EmptyResult<NetworkError.APIError>
    suspend fun deleteReminder(reminder: Reminder): EmptyResult<NetworkError.APIError>
}