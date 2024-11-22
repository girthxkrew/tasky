package com.rkm.tasky.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.rkm.tasky.database.model.SyncDeletePhotoEntity

@Dao
interface SyncDeletePhotoDao {

    @Query("SELECT * FROM sync_delete_photos WHERE eventId = :id")
    suspend fun getPhotoKeysByEventId(id: String): List<SyncDeletePhotoEntity>

    @Upsert
    suspend fun upsertPhotoKeys(photos: List<SyncDeletePhotoEntity>)

    @Query("DELETE FROM sync_delete_photos WHERE eventId = :id")
    suspend fun deletePhotoKeysByEventId(id: String)
}