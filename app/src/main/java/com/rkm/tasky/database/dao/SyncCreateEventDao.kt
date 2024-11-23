package com.rkm.tasky.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.rkm.tasky.database.model.SyncCreateEventEntity
import com.rkm.tasky.database.model.SyncCreateEventWithDetails

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

    @Query("DELETE from sync_create_event WHERE id IN (:ids)")
    suspend fun deleteEventsById(ids: List<String>)
}