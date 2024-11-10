package com.rkm.tasky.repository.model

import kotlinx.datetime.LocalDateTime

data class Reminder(
    val id: String,
    val title: String,
    val description: String?,
    val time: LocalDateTime,
    val remindAt: LocalDateTime
)
