package com.rkm.tasky.repository.implementation

import com.rkm.tasky.database.dao.AttendeeDao
import com.rkm.tasky.database.dao.EventDao
import com.rkm.tasky.database.dao.PhotoDao
import com.rkm.tasky.database.dao.SyncDao
import com.rkm.tasky.database.error.DatabaseError
import com.rkm.tasky.database.model.SyncEntity
import com.rkm.tasky.database.model.SyncItemType
import com.rkm.tasky.database.model.SyncUserAction
import com.rkm.tasky.di.IoDispatcher
import com.rkm.tasky.feature.event.model.Event
import com.rkm.tasky.network.datasource.TaskyEventRemoteDataSource
import com.rkm.tasky.network.util.NetworkError
import com.rkm.tasky.network.util.asMultiPartBody
import com.rkm.tasky.network.util.safeCall
import com.rkm.tasky.repository.abstraction.TaskyEventRepository
import com.rkm.tasky.repository.mapper.asAttendeeEntity
import com.rkm.tasky.repository.mapper.asCreateEventRequest
import com.rkm.tasky.repository.mapper.asEventEntity
import com.rkm.tasky.repository.mapper.asPhotoEntity
import com.rkm.tasky.repository.mapper.asSyncCreateEventRequest
import com.rkm.tasky.repository.mapper.asSyncUpdateEventRequest
import com.rkm.tasky.repository.mapper.asUpdateEventRequest
import com.rkm.tasky.repository.mapper.toEvent
import com.rkm.tasky.sync.repository.abstraction.SyncCreateEventRepository
import com.rkm.tasky.sync.repository.abstraction.SyncUpdateEventRepository
import com.rkm.tasky.util.result.EmptyResult
import com.rkm.tasky.util.result.Result
import com.rkm.tasky.util.result.asEmptyDataResult
import com.rkm.tasky.util.result.onFailure
import com.rkm.tasky.util.result.onSuccess
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TaskyEventRepositoryImpl @Inject constructor(
    private val remoteDataSource: TaskyEventRemoteDataSource,
    private val localDataSource: EventDao,
    private val attendeeLocalDataSource: AttendeeDao,
    private val photoLocalDataSource: PhotoDao,
    private val syncCreateEventDataSource: SyncCreateEventRepository,
    private val syncUpdateEventDataSource: SyncUpdateEventRepository,
    private val syncDataSource: SyncDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : TaskyEventRepository {
    override suspend fun getEvent(id: String): Result<Event, DatabaseError.ItemError> =
        withContext(dispatcher) {
            return@withContext when (val results = localDataSource.getAllEventDetails(id)) {
                null -> Result.Error(DatabaseError.ItemError.ITEM_DOES_NOT_EXIST)
                else -> Result.Success(results.toEvent())
            }
        }

    override suspend fun createEvent(event: Event): EmptyResult<NetworkError.APIError> =
        withContext(dispatcher) {
            val result = safeCall {
                remoteDataSource.createEvent(
                    event.asCreateEventRequest(),
                    event.photosToUpload.asMultiPartBody()
                )
            }
            result.onSuccess {
                localDataSource.upsertAllEventInfo(
                    event = it.asEventEntity(),
                    attendees = it.attendees.map { attendee -> attendee.asAttendeeEntity() },
                    photos = it.photos.map { photo -> photo.asPhotoEntity(it.id) },
                    photoDao = photoLocalDataSource,
                    attendeeDao = attendeeLocalDataSource
                )
            }

            result.onFailure {
                syncCreateEventDataSource.upsertEvent(event.asSyncCreateEventRequest())
            }

            return@withContext result.asEmptyDataResult()
        }

    override suspend fun updateEvent(event: Event): EmptyResult<NetworkError.APIError> =
        withContext(dispatcher) {
            val result = safeCall {
                remoteDataSource.updateEvent(
                    event.asUpdateEventRequest(),
                    event.photosToUpload.asMultiPartBody()
                )
            }
            result.onSuccess {
                localDataSource.upsertAllEventInfo(
                    event = it.asEventEntity(),
                    attendees = it.attendees.map { attendee -> attendee.asAttendeeEntity() },
                    photos = it.photos.map { photo -> photo.asPhotoEntity(it.id) },
                    photoDao = photoLocalDataSource,
                    attendeeDao = attendeeLocalDataSource
                )
            }

            result.onFailure {
                syncUpdateEventDataSource.upsertEvent(event.asSyncUpdateEventRequest())
            }

            photoLocalDataSource.deletePhotos(event.deletedPhotoKeys)
            return@withContext result.asEmptyDataResult()
        }

    override suspend fun deleteEvent(event: Event): EmptyResult<NetworkError.APIError> = withContext(dispatcher) {
        val result = safeCall { remoteDataSource.deleteEvent(event.id) }
        result.onFailure {
            syncDataSource.upsertSyncItem(
                SyncEntity(
                    action = SyncUserAction.DELETE,
                    item = SyncItemType.EVENT,
                    itemId = event.id
                )
            )
        }
        localDataSource.deleteEvent(event.asEventEntity())
        return@withContext result.asEmptyDataResult()
    }
}