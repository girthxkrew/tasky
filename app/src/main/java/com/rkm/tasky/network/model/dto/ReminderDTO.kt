package com.rkm.tasky.network.model.dto

import com.rkm.tasky.network.model.response.ReminderResponse

data class ReminderDTO(
    val id: String,
    val title: String,
    val description: String?,
    val time: Long,
    val remindAt: Long
)

fun ReminderResponse.asReminderDTO(): ReminderDTO {
    return ReminderDTO(
        id = this.id,
        title = this.title,
        description = this.description,
        time = this.time,
        remindAt = this.remindAt
    )
}