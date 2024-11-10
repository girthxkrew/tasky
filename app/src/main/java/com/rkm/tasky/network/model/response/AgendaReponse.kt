package com.rkm.tasky.network.model.response

import com.rkm.tasky.network.model.TaskRemote

data class AgendaResponse(
    val events: List<EventResponse>,
    val taskRemote: List<TaskRemote>,
    val reminderRemotes: List<ReminderResponse>
)
