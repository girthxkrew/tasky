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
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
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
        listOf(
            launch { syncDataSource.upsertSyncItem(
                SyncEntity(
                    action = SyncUserAction.CREATE,
                    item = SyncItemType.EVENT,
                    itemId = event.id
                )
            ) },
            launch { attendeeDataSource.upsertAttendees(event.asSyncAttendeeEntity()) },
            launch { photoDataSource.upsertPhotos(event.asUploadPhotosEntity()) },
            launch { eventDataSource.upsertEvent(event.asSyncCreateEventEntity()) }
        ).joinAll()
    }

    override suspend fun deleteEvents(ids: List<String>) = withContext(dispatcher) {
        listOf(
            launch { syncDataSource.deleteSyncItems(ids) },
            launch { attendeeDataSource.deleteAttendeesById(ids) },
            launch { photoDataSource.deletePhotosByEventId(ids) },
            launch { eventDataSource.deleteEventsById(ids) }
        ).joinAll()
    }
}