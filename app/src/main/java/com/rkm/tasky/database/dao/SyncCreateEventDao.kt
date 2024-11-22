package com.rkm.tasky.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.rkm.tasky.database.model.SyncCreateEventEntity

@Dao
interface SyncCreateEventDao {

    @Query("SELECT * FROM sync_create_event WHERE id = :id")
    suspend fun getEventById(id: String): SyncCreateEventEntity

    @Upsert
    suspend fun upsertEvent(event: SyncCreateEventEntity)

    @Query("DELETE from sync_create_event WHERE id = :id")
    suspend fun deleteEventById(id: String)
}