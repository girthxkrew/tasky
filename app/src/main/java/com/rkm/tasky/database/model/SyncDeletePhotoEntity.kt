package com.rkm.tasky.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "sync_delete_photos"
)
data class SyncDeletePhotoEntity(
    val eventId: String,
    @PrimaryKey
    val photoKey: String
)
