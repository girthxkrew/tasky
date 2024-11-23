package com.rkm.tasky.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.RoomWarnings
import androidx.room.Transaction
import androidx.room.Upsert
import com.rkm.tasky.database.model.EventEntity
import com.rkm.tasky.database.model.EventWithDetails

@Dao
interface EventDao {

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Transaction
    @Query("SELECT * FROM events " +
            "JOIN attendees ON events.id = attendees.eventId " +
            "JOIN photos ON events.id = photos.eventId where events.id = :id")
    suspend fun getAllEventDetails(id: String): EventWithDetails?

    @Query("SELECT * FROM events where id = :id")
    suspend fun getEventById(id: String): EventEntity

    @Upsert
    suspend fun upsertEvent(event: EventEntity)

    @Delete
    suspend fun deleteEvent(event: EventEntity)

    @Query("DELETE FROM events WHERE id IN (:ids)")
    suspend fun deleteEventsByIds(ids: List<String>)
}