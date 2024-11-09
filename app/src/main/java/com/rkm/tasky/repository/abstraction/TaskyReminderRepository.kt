package com.rkm.tasky.repository.abstraction

import com.rkm.tasky.database.model.ReminderEntity
import com.rkm.tasky.network.model.Reminder
import com.rkm.tasky.network.util.NetworkError
import com.rkm.tasky.util.result.EmptyResult
import com.rkm.tasky.util.result.Result
import java.util.UUID

interface TaskyReminderRepository {
    suspend fun getReminder(id: String): Result<ReminderDTO?, NetworkError.APIError>
    suspend fun createReminder(reminder: ReminderDTO): EmptyResult<NetworkError.APIError>
    suspend fun updateReminder(reminder: ReminderDTO): EmptyResult<NetworkError.APIError>
    suspend fun deleteReminder(reminder: ReminderDTO): EmptyResult<NetworkError.APIError>
}

data class ReminderDTO(
    val id: String,
    val title: String,
    val description: String?,
    val time: Long,
    val remindAt: Long
)

fun ReminderDTO.asReminder(): Reminder {
    return Reminder(
        id = this.id,
        title = this.title,
        description = this.description,
        time = this.time,
        remindAt = this.remindAt
    )
}

fun Reminder.asReminderDTO(): ReminderDTO {
    return ReminderDTO(
        id = this.id,
        title = this.title,
        description = this.description,
        time = this.time,
        remindAt = this.remindAt
    )
}

fun ReminderDTO.asReminderEntity(): ReminderEntity {
    return ReminderEntity(
        id = this.id,
        title = this.title,
        description = this.description,
        time = this.time,
        remindAt = remindAt
    )
}

fun ReminderEntity.asReminderDTO(): ReminderDTO {
    return ReminderDTO(
        id = this.id,
        title = this.title,
        description = this.description,
        time = this.time,
        remindAt = this.remindAt
    )
}