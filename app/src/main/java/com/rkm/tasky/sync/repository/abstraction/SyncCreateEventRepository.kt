package com.rkm.tasky.sync.repository.abstraction

import com.rkm.tasky.database.error.DatabaseError
import com.rkm.tasky.database.model.SyncCreateEventWithDetails
import com.rkm.tasky.sync.model.CreateEventSyncRequest
import com.rkm.tasky.util.result.Result

interface SyncCreateEventRepository {
    suspend fun getEvents(ids: List<String>): Result<List<SyncCreateEventWithDetails>, DatabaseError.ItemError>
    suspend fun upsertEvent(event: CreateEventSyncRequest)
    suspend fun deleteEvents(ids: List<String>)
}