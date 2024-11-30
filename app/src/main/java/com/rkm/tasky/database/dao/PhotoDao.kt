package com.rkm.tasky.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.rkm.tasky.database.model.PhotoEntity

@Dao
interface PhotoDao {

    @Query("SELECT * FROM photos WHERE eventId = (:ids)")
    suspend fun getPhotosByEventId(ids: List<String>): List<PhotoEntity>

    @Upsert
    suspend fun upsertPhotos(photos: List<PhotoEntity>)

    @Query("DELETE FROM photos WHERE `key` IN (:keys)")
    suspend fun deletePhotos(keys: List<String>)
}