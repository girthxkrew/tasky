package com.rkm.tasky.network.model.response

data class AgendaDTO(
    val events: List<EventDTO>,
    val taskRemote: List<TaskDTO>,
    val reminderRemotes: List<ReminderDTO>
)
