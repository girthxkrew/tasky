package com.rkm.tasky.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.rkm.tasky.database.model.SyncAttendeeEntity

@Dao
interface SyncAttendeeDao {

    @Query("SELECT * FROM sync_attendee WHERE eventId = :id")
    suspend fun getAttendsForEvent(id: String): List<SyncAttendeeEntity>

    @Upsert
    suspend fun upsertAttendees(attendees: List<SyncAttendeeEntity>)

    @Query("DELETE from sync_attendee WHERE eventId IN (:ids) ")
    suspend fun deleteAttendeesById(ids: List<String>)
}