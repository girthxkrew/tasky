package com.rkm.tasky.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "reminders",
)
data class ReminderEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String?,
    val time: Long,
    val remindAt: Long
)
