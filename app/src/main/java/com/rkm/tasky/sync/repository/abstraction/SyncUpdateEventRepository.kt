package com.rkm.tasky.sync.repository.abstraction

import com.rkm.tasky.database.error.DatabaseError
import com.rkm.tasky.database.model.SyncUpdateEventWithDetails
import com.rkm.tasky.sync.model.UpdateEventSyncRequest
import com.rkm.tasky.util.result.Result

interface SyncUpdateEventRepository {
    suspend fun getEvents(ids: List<String>): Result<List<SyncUpdateEventWithDetails>, DatabaseError.ItemError>
    suspend fun upsertEvent(event: UpdateEventSyncRequest)
    suspend fun deleteEvents(ids: List<String>)
}