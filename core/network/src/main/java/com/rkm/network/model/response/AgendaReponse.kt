package com.rkm.network.model.response

import com.rkm.network.model.Reminder
import com.rkm.network.model.Task

data class AgendaResponse(
    val events: List<EventResponse>,
    val task: List<Task>,
    val reminders: List<Reminder>
)
