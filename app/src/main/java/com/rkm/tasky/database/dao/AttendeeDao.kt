package com.rkm.tasky.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.rkm.tasky.database.model.AttendeeEntity

@Dao
interface AttendeeDao {

    @Query("SELECT * FROM ATTENDEES where eventId = :id")
    suspend fun getAttendeeByEventId(id: String): List<AttendeeEntity>

    @Upsert
    suspend fun upsertAttendees(attendees: List<AttendeeEntity>)

    @Delete
    suspend fun deleteAttendees(attendees: List<AttendeeEntity>)
}