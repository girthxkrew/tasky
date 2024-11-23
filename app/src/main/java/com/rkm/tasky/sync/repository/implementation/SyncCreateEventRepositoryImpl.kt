package com.rkm.tasky.sync.repository.implementation

import com.rkm.tasky.database.dao.SyncAttendeeDao
import com.rkm.tasky.database.dao.SyncCreateEventDao
import com.rkm.tasky.database.dao.SyncDao
import com.rkm.tasky.database.dao.SyncUploadPhotoDao
import com.rkm.tasky.database.error.DatabaseError
import com.rkm.tasky.database.model.SyncCreateEventWithDetails
import com.rkm.tasky.database.model.SyncEntity
import com.rkm.tasky.database.model.SyncItemType
import com.rkm.tasky.database.model.SyncUserAction
import com.rkm.tasky.di.IoDispatcher
import com.rkm.tasky.sync.mapper.asSyncAttendeeEntity
import com.rkm.tasky.sync.mapper.asSyncCreateEventEntity
import com.rkm.tasky.sync.mapper.asUploadPhotosEntity
import com.rkm.tasky.sync.model.CreateEventSyncRequest
import com.rkm.tasky.sync.repository.abstraction.SyncCreateEventRepository
import com.rkm.tasky.util.result.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SyncCreateEventRepositoryImpl @Inject constructor(
    private val syncDataSource: SyncDao,
    private val attendeeDataSource: SyncAttendeeDao,
    private val photoDataSource: SyncUploadPhotoDao,
    private val eventDataSource: SyncCreateEventDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : SyncCreateEventRepository {
    override suspend fun getEvents(ids: List<String>): Result<List<SyncCreateEventWithDetails>, DatabaseError.ItemError> =
        withContext(dispatcher) {
            val result = eventDataSource.getEventDetailsById(ids)
            return@withContext Result.Success(result)
        }

    override suspend fun upsertEvent(event: CreateEventSyncRequest) = withContext(dispatcher) {
        syncDataSource.upsertSyncItem(
            SyncEntity(
                action = SyncUserAction.CREATE,
                item = SyncItemType.EVENT,
                itemId = event.id
            )
        )
        attendeeDataSource.upsertAttendees(event.asSyncAttendeeEntity())
        photoDataSource.upsertPhotos(event.asUploadPhotosEntity())
        eventDataSource.upsertEvent(event.asSyncCreateEventEntity())
    }

    override suspend fun deleteEvents(ids: List<String>) = withContext(dispatcher) {
        syncDataSource.deleteSyncItems(ids)
        attendeeDataSource.deleteAttendeesById(ids)
        photoDataSource.deletePhotosByEventId(ids)
        eventDataSource.deleteEventsById(ids)
    }
}