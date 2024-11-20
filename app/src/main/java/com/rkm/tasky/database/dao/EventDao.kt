package com.rkm.tasky.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.rkm.tasky.database.model.EventEntity
import com.rkm.tasky.database.model.EventWithDetails

@Dao
interface EventDao {

    @Query("SELECT * FROM events, attendees, photos where id = :id")
    suspend fun getAllEventDetails(id: String): EventWithDetails

    @Query("SELECT * FROM events where id = :id")
    suspend fun getEventById(id: String)

    @Upsert
    suspend fun upsertEvent(event: EventEntity)

    @Delete
    suspend fun deleteEvent(event: EventEntity)
}