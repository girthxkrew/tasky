package com.rkm.tasky.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.rkm.tasky.database.model.PhotoEntity

@Dao
interface PhotoDao {

    @Query("SELECT * FROM photos WHERE eventId = :id")
    suspend fun getPhotosByEventId(id: String): List<PhotoEntity>

    @Upsert
    suspend fun upsertPhotos(photos: List<PhotoEntity>)

    @Delete
    suspend fun deletePhotos(photo: PhotoEntity)
}