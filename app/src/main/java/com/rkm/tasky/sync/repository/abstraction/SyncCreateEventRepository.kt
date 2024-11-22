package com.rkm.tasky.sync.repository.abstraction

import com.rkm.tasky.database.error.DatabaseError
import com.rkm.tasky.sync.model.CreateEventSyncRequest
import com.rkm.tasky.util.result.Result

interface SyncCreateEventRepository {
    suspend fun getEvent(id: String): Result<CreateEventSyncRequest, DatabaseError.ItemError>
    suspend fun upsertEvent(event: CreateEventSyncRequest)
    suspend fun deleteEvent(id: String)
}