package com.rkm.tasky.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "sync_upload_photos"
)
data class SyncUploadPhotoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val eventId: String,
    val filePath: String
)
