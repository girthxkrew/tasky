package com.rkm.tasky.repository.mapper

import ReminderRequest
import com.rkm.tasky.database.model.ReminderEntity
import com.rkm.tasky.network.model.dto.ReminderDTO
import com.rkm.tasky.repository.model.Reminder
import com.rkm.tasky.util.date.toLocalDateTime
import com.rkm.tasky.util.date.toLong

fun ReminderDTO.asReminderEntity(): ReminderEntity {
    return ReminderEntity(
        id = this.id,
        title = this.title,
        description = this.description,
        time = this.time,
        remindAt = this.remindAt
    )
}

fun ReminderDTO.asReminder(): Reminder {
    return Reminder(
        id = this.id,
        title = this.title,
        description = this.description,
        time = time.toLocalDateTime(),
        remindAt = this.remindAt.toLocalDateTime()
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

fun ReminderEntity.asReminder(): Reminder {
    return Reminder(
        id = this.id,
        title = this.title,
        description = this.description,
        time = this.time.toLocalDateTime(),
        remindAt = this.remindAt.toLocalDateTime()
    )
}

fun Reminder.asReminderEntity(): ReminderEntity {
    return ReminderEntity(
        id = this.id,
        title = this.title,
        description = this.description,
        time = this.time.toLong(),
        remindAt = this.remindAt.toLong()
    )
}

fun Reminder.asReminderRequest(): ReminderRequest {
    return ReminderRequest(
        id = this.id,
        title = this.title,
        description = this.description,
        time = this.time.toLong(),
        remindAt = this.remindAt.toLong()
    )
}