package com.rkm.tasky.sync.repository.implementation

import com.rkm.tasky.database.dao.SyncAttendeeDao
import com.rkm.tasky.database.dao.SyncDao
import com.rkm.tasky.database.dao.SyncDeletePhotoDao
import com.rkm.tasky.database.dao.SyncUpdateEventDao
import com.rkm.tasky.database.dao.SyncUploadPhotoDao
import com.rkm.tasky.database.error.DatabaseError
import com.rkm.tasky.database.model.SyncEntity
import com.rkm.tasky.database.model.SyncItemType
import com.rkm.tasky.database.model.SyncUploadPhotoEntity
import com.rkm.tasky.database.model.SyncUserAction
import com.rkm.tasky.di.IoDispatcher
import com.rkm.tasky.sync.mapper.asSyncAttendeeEntity
import com.rkm.tasky.sync.mapper.asSyncDeletePhotoEntity
import com.rkm.tasky.sync.mapper.asSyncUpdateEventEntity
import com.rkm.tasky.sync.mapper.asSyncUploadPhotosEntity
import com.rkm.tasky.sync.model.UpdateEventSyncRequest
import com.rkm.tasky.sync.repository.abstraction.SyncUpdateEventRepository
import com.rkm.tasky.util.result.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SyncUpdateEventRepositoryImpl @Inject constructor(
    private val syncDataSource: SyncDao,
    private val attendeesDataSource: SyncAttendeeDao,
    private val deletedPhotosDataSource: SyncDeletePhotoDao,
    private val uploadPhotoDataSource: SyncUploadPhotoDao,
    private val eventDataSource: SyncUpdateEventDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
): SyncUpdateEventRepository {
    override suspend fun getEvent(id: String): Result<UpdateEventSyncRequest, DatabaseError.ItemError> {
        TODO("Not yet implemented")
    }

    override suspend fun upsertEvent(event: UpdateEventSyncRequest) = withContext(dispatcher) {
        syncDataSource.upsertSyncItem(
            SyncEntity(
                action = SyncUserAction.UPDATE,
                item = SyncItemType.EVENT,
                itemId = event.id
            )
        )
        attendeesDataSource.upsertAttendees(event.asSyncAttendeeEntity())
        deletedPhotosDataSource.upsertPhotoKeys(event.asSyncDeletePhotoEntity())
        uploadPhotoDataSource.upsertPhotos(event.asSyncUploadPhotosEntity())
        eventDataSource.upsertEvent(event.asSyncUpdateEventEntity())
    }

    override suspend fun deleteEvent(id: String) = withContext(dispatcher) {
        syncDataSource.deleteSyncItem(id)
        attendeesDataSource.deleteAttendeesById(id)
        deletedPhotosDataSource.deletePhotoKeysByEventId(id)
        uploadPhotoDataSource.deletePhotosByEventId(id)
        eventDataSource.deleteEventById(id)
    }
}