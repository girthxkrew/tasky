package com.rkm.tasky.repository.abstraction

import com.rkm.tasky.database.error.DatabaseError
import com.rkm.tasky.feature.event.model.Event
import com.rkm.tasky.network.util.NetworkError
import com.rkm.tasky.util.result.EmptyResult
import com.rkm.tasky.util.result.Result

interface TaskyEventRepository {
    suspend fun getEvent(id: String): Result<Event, DatabaseError.ItemError>
    suspend fun createEvent(event: Event): EmptyResult<NetworkError.APIError>
    suspend fun updateEvent(event: Event): EmptyResult<NetworkError.APIError>
    suspend fun deleteEvent(event: Event): EmptyResult<NetworkError.APIError>
}