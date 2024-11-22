package com.rkm.tasky.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "sync_attendee"
)
data class SyncAttendeeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val eventId: String,
    val userId: String
)
