package com.rkm.tasky.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.rkm.tasky.database.model.SyncUploadPhotoEntity

@Dao
interface SyncUploadPhotoDao {

    @Query("SELECT * FROM sync_upload_photos WHERE eventId IN (:ids)")
    suspend fun getPhotosByEventId(ids: List<String>): List<SyncUploadPhotoEntity>

    @Upsert
    suspend fun upsertPhotos(photos: List<SyncUploadPhotoEntity>)

    @Query("DELETE FROM sync_upload_photos WHERE eventId IN (:ids)")
    suspend fun deletePhotosByEventId(ids: List<String>)
}