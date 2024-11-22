package com.rkm.tasky.sync.repository.implementation

import com.rkm.tasky.database.dao.SyncAttendeeDao
import com.rkm.tasky.database.dao.SyncCreateEventDao
import com.rkm.tasky.database.dao.SyncDao
import com.rkm.tasky.database.dao.SyncUploadPhotoDao
import com.rkm.tasky.database.error.DatabaseError
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
): SyncCreateEventRepository {
    override suspend fun getEvent(id: String): Result<CreateEventSyncRequest, DatabaseError.ItemError> {
        TODO("Not yet implemented")
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

    override suspend fun deleteEvent(id: String) = withContext(dispatcher) {
        syncDataSource.deleteSyncItem(id)
        attendeeDataSource.deleteAttendeesById(id)
        photoDataSource.deletePhotosByEventId(id)
        eventDataSource.deleteEventById(id)
    }
}