package com.rkm.tasky.network.model.response

import com.rkm.tasky.network.Reminder
import com.rkm.tasky.network.Task

data class AgendaResponse(
    val events: List<EventResponse>,
    val task: List<Task>,
    val reminders: List<Reminder>
)
