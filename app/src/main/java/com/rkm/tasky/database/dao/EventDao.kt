package com.rkm.tasky.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.RoomWarnings
import androidx.room.Transaction
import androidx.room.Upsert
import com.rkm.tasky.database.model.AttendeeEntity
import com.rkm.tasky.database.model.EventEntity
import com.rkm.tasky.database.model.EventWithDetails
import com.rkm.tasky.database.model.PhotoEntity

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

    @Transaction
    suspend fun upsertAllEventInfo(
        event: EventEntity,
        attendees: List<AttendeeEntity>,
        photos: List<PhotoEntity>,
        photoDao: PhotoDao,
        attendeeDao: AttendeeDao,
    ) {
        upsertEvent(event)
        val oldAttendees = attendeeDao.getAttendeesByEventId(listOf(event.id))
        if(oldAttendees.isNotEmpty()) {
            attendeeDao.deleteAttendees(oldAttendees.subtract(attendees).toList())
        }
        attendeeDao.upsertAttendees(oldAttendees)
        val oldPhotos = photoDao.getPhotosByEventId(listOf(event.id))
        if(oldPhotos.isNotEmpty()) {
            photoDao.deletePhotos(oldPhotos.subtract(photos).map { it.key })
        }
        photoDao.upsertPhotos(photos)
    }

    @Delete
    suspend fun deleteEvent(event: EventEntity)

    @Query("DELETE FROM events WHERE id IN (:ids)")
    suspend fun deleteEventsByIds(ids: List<String>)
}