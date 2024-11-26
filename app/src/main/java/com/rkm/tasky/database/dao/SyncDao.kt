package com.rkm.tasky.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.rkm.tasky.database.model.SyncEntity

@Dao
interface SyncDao {

    @Query("SELECT * FROM sync")
    suspend fun getAllSyncItems(): List<SyncEntity>

    @Query("SELECT * FROM sync WHERE itemId = :id")
    suspend fun getSyncItemById(id: String): SyncEntity

    @Query("SELECT * FROM sync WHERE `action` = :action")
    suspend fun getSyncItemsByAction(action: String): List<SyncEntity>

    @Upsert
    suspend fun upsertSyncItem(item: SyncEntity)

    @Query("DELETE FROM sync WHERE itemId = :ids")
    suspend fun deleteSyncItems(ids: List<String>)
}