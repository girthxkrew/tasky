package com.rkm.tasky.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.rkm.tasky.database.model.SyncUpdateEventEntity
import com.rkm.tasky.database.model.SyncUpdateEventWithDetails

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

    @Query("DELETE FROM sync_update_event WHERE id IN (:ids)")
    suspend fun deleteEventsById(ids: List<String>)
}