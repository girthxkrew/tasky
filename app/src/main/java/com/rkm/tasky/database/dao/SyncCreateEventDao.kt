package com.rkm.tasky.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.rkm.tasky.database.model.SyncAttendeeEntity
import com.rkm.tasky.database.model.SyncCreateEventEntity
import com.rkm.tasky.database.model.SyncCreateEventWithDetails
import com.rkm.tasky.database.model.SyncEntity
import com.rkm.tasky.database.model.SyncUploadPhotoEntity

@Dao
interface SyncCreateEventDao {

    @Query("SELECT * FROM sync_create_event WHERE id IN (:ids)")
    suspend fun getEventsById(ids: List<String>): List<SyncCreateEventEntity>

    @Query("SELECT * FROM sync_create_event WHERE id = :id")
    suspend fun getEventById(id: String): SyncCreateEventEntity

    @Transaction
    @Query("SELECT * FROM sync_create_event WHERE id IN (:ids)")
    suspend fun getEventDetailsById(ids: List<String>): List<SyncCreateEventWithDetails>

    @Upsert
    suspend fun upsertEvent(event: SyncCreateEventEntity)

    @Transaction
    suspend fun upsertEventDetailsById(
        event: SyncCreateEventEntity,
        attendees: List<SyncAttendeeEntity>,
        photos: List<SyncUploadPhotoEntity>,
        syncEntity: SyncEntity,
        syncDao: SyncDao,
        attendeeDao: SyncAttendeeDao,
        photoDao: SyncUploadPhotoDao
    ) {
        upsertEvent(event)
        syncDao.upsertSyncItem(syncEntity)
        attendeeDao.upsertAttendees(attendees)
        photoDao.upsertPhotos(photos)
    }

    @Query("DELETE from sync_create_event WHERE id IN (:ids)")
    suspend fun deleteEventsById(ids: List<String>)

    @Transaction
    suspend fun deleteEventDetailsById(
        ids: List<String>,
        attendeeDao: SyncAttendeeDao,
        photoDao: SyncUploadPhotoDao
    ) {
        deleteEventsById(ids)
        attendeeDao.deleteAttendeesById(ids)
        photoDao.deletePhotosByEventId(ids)
    }
}