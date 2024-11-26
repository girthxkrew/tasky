package com.rkm.tasky.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.rkm.tasky.database.model.SyncAttendeeEntity
import com.rkm.tasky.database.model.SyncDeletePhotoEntity
import com.rkm.tasky.database.model.SyncEntity
import com.rkm.tasky.database.model.SyncUpdateEventEntity
import com.rkm.tasky.database.model.SyncUpdateEventWithDetails
import com.rkm.tasky.database.model.SyncUploadPhotoEntity

@Dao
interface SyncUpdateEventDao {

    @Query("SELECT * FROM sync_update_event WHERE id IN (:ids)")
    suspend fun getEventsById(ids: List<String>): List<SyncUpdateEventEntity>

    @Query("SELECT * FROM sync_update_event WHERE id = :id")
    suspend fun getEventById(id: String): SyncUpdateEventEntity

    @Transaction
    @Query("SELECT * FROM sync_update_event WHERE id IN (:ids)")
    suspend fun getEventDetailsById(ids: List<String>): List<SyncUpdateEventWithDetails>

    @Upsert
    suspend fun upsertEvent(event: SyncUpdateEventEntity)

    @Transaction
    suspend fun upsertEventDetailsByIds(
        event: SyncUpdateEventEntity,
        attendees: List<SyncAttendeeEntity>,
        photos: List<SyncUploadPhotoEntity>,
        photoKeys: List<SyncDeletePhotoEntity>,
        syncEntity: SyncEntity,
        syncDao: SyncDao,
        attendeeDao: SyncAttendeeDao,
        photoDao: SyncUploadPhotoDao,
        deletePhotoDao: SyncDeletePhotoDao
    ) {
        upsertEvent(event)
        syncDao.upsertSyncItem(syncEntity)
        attendeeDao.upsertAttendees(attendees)
        photoDao.upsertPhotos(photos)
        deletePhotoDao.upsertPhotoKeys(photoKeys)
    }

    @Query("DELETE FROM sync_update_event WHERE id IN (:ids)")
    suspend fun deleteEventsById(ids: List<String>)

    @Transaction
    suspend fun deleteEventDetailsByIds(
        ids: List<String>,
        attendeeDao: SyncAttendeeDao,
        photoDao: SyncUploadPhotoDao,
        deletePhotoDao: SyncDeletePhotoDao
    ) {
        deleteEventsById(ids)
        attendeeDao.deleteAttendeesById(ids)
        photoDao.deletePhotosByEventId(ids)
        deletePhotoDao.deletePhotoKeysByEventId(ids)
    }
}