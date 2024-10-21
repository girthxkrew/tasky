package com.rkm.tasky.network.model.response

import com.rkm.tasky.network.model.Reminder
import com.rkm.tasky.network.model.Task

data class AgendaResponse(
    val events: List<EventResponse>,
    val task: List<Task>,
    val reminders: List<Reminder>
)
