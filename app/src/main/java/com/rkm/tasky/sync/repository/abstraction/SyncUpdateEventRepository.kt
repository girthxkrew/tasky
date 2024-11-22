package com.rkm.tasky.sync.repository.abstraction

import com.rkm.tasky.database.error.DatabaseError
import com.rkm.tasky.sync.model.UpdateEventSyncRequest
import com.rkm.tasky.util.result.Result

interface SyncUpdateEventRepository {
    suspend fun getEvent(id: String): Result<UpdateEventSyncRequest, DatabaseError.ItemError>
    suspend fun upsertEvent(event: UpdateEventSyncRequest)
    suspend fun deleteEvent(id: String)
}