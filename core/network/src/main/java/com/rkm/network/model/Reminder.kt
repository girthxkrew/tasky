package com.rkm.network.model

data class Reminder(
    val id: String,
    val title: String,
    val description: String?,
    val time: Long,
    val remindAt: Long
)
