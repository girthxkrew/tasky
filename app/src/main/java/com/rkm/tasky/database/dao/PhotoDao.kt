package com.rkm.tasky.database.dao

import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.rkm.tasky.database.model.PhotoEntity

interface PhotoDao {

    @Query("SELECT * FROM photos WHERE eventId = :id")
    suspend fun getPhotosByEventId(id: String): List<PhotoEntity>

    @Upsert
    suspend fun upsertPhotos(photos: List<PhotoEntity>)

    @Delete
    suspend fun deletePhotos(photos: List<PhotoEntity>)
}