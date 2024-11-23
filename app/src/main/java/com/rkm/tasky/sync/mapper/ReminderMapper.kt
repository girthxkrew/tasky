package com.rkm.tasky.sync.mapper

import ReminderRequest
import com.rkm.tasky.database.model.ReminderEntity

fun ReminderEntity.asReminderRequest(): ReminderRequest {
    return ReminderRequest(
        id = this.id,
        title = this.title,
        description = this.description,
        time = this.time,
        remindAt = this.remindAt
    )
}