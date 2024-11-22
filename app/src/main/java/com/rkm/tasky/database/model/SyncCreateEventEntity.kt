package com.rkm.tasky.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sync_create_event")
data class SyncCreateEventEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    val from: Long,
    val to: Long,
    val remindAt: Long
)
