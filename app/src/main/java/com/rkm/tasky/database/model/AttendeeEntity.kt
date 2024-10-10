package com.rkm.tasky.database.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "attendees",
    foreignKeys = [
        ForeignKey(
            entity = EventEntity::class,
            parentColumns = ["id"],
            childColumns = ["eventId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class AttendeeEntity(
    @PrimaryKey
    val userId: String,
    val email: String,
    val fullName: String,
    val eventId: String,
    val isGoing: Boolean,
    val remindAt: Long
)
