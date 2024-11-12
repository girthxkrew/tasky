package com.rkm.tasky.feature.task.model

import kotlinx.datetime.LocalDateTime

data class Task(
    val id: String,
    val title: String,
    val description: String?,
    val time: LocalDateTime,
    val remindAt: LocalDateTime,
    val isDone: Boolean
)
