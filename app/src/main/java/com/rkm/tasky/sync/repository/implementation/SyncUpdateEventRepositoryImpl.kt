package com.rkm.tasky.sync.repository.implementation

import com.rkm.tasky.database.dao.SyncAttendeeDao
import com.rkm.tasky.database.dao.SyncDao
import com.rkm.tasky.database.dao.SyncDeletePhotoDao
import com.rkm.tasky.database.dao.SyncUpdateEventDao
import com.rkm.tasky.database.dao.SyncUploadPhotoDao
import com.rkm.tasky.database.error.DatabaseError
import com.rkm.tasky.database.model.SyncEntity
import com.rkm.tasky.database.model.SyncItemType
import com.rkm.tasky.database.model.SyncUpdateEventWithDetails
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
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SyncUpdateEventRepositoryImpl @Inject constructor(
    private val syncDataSource: SyncDao,
    private val attendeesDataSource: SyncAttendeeDao,
    private val deletedPhotosDataSource: SyncDeletePhotoDao,
    private val uploadPhotoDataSource: SyncUploadPhotoDao,
    private val eventDataSource: SyncUpdateEventDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : SyncUpdateEventRepository {
    override suspend fun getEvents(ids: List<String>): Result<List<SyncUpdateEventWithDetails>, DatabaseError.ItemError> =
        withContext(dispatcher) {
            val result = eventDataSource.getEventDetailsById(ids)
            return@withContext Result.Success(result)
        }

    override suspend fun upsertEvent(event: UpdateEventSyncRequest) = withContext(dispatcher) {
        eventDataSource.upsertEventDetailsByIds(
            event = event.asSyncUpdateEventEntity(),
            attendees = event.asSyncAttendeeEntity(),
            photos = event.asSyncUploadPhotosEntity(),
            photoKeys = event.asSyncDeletePhotoEntity(),
            syncEntity = SyncEntity(
                action = SyncUserAction.UPDATE,
                item = SyncItemType.EVENT,
                itemId = event.id
            ),
            syncDao = syncDataSource,
            attendeeDao = attendeesDataSource,
            photoDao = uploadPhotoDataSource,
            deletePhotoDao = deletedPhotosDataSource
        )
    }

    override suspend fun deleteEvents(ids: List<String>) = withContext(dispatcher) {
        eventDataSource.deleteEventDetailsByIds(
            ids,
            attendeesDataSource,
            uploadPhotoDataSource,
            deletedPhotosDataSource
        )
    }
}