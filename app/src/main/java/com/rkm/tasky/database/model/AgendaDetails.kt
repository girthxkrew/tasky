package com.rkm.tasky.database.model

data class AgendaDetails(
    val events: List<EventWithDetails> = emptyList(),
    val reminders: List<ReminderEntity> = emptyList(),
    val tasks: List<TaskEntity> = emptyList()
)
