package com.rkm.tasky.network.model.response

data class AgendaDTO(
    val events: List<EventDTO>,
    val tasks: List<TaskDTO>,
    val reminders: List<ReminderDTO>
)
