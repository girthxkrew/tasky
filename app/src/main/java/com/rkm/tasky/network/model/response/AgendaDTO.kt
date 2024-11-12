package com.rkm.tasky.network.model.response

import com.rkm.tasky.network.model.TaskRemote

data class AgendaDTO(
    val events: List<EventDTO>,
    val taskRemote: List<TaskRemote>,
    val reminderRemotes: List<ReminderDTO>
)
