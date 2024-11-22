package com.rkm.tasky.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.rkm.tasky.database.model.SyncUpdateEventEntity

@Dao
interface SyncUpdateEventDao {

    @Query("SELECT * FROM sync_update_event WHERE id = :id")
    suspend fun getEventById(id: String): SyncUpdateEventEntity

    @Upsert
    suspend fun upsertEvent(event: SyncUpdateEventEntity)

    @Query("DELETE FROM sync_update_event WHERE id = :id")
    suspend fun deleteEventById(id: String)
}