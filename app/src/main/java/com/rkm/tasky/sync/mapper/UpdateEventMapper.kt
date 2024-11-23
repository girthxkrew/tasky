package com.rkm.tasky.sync.mapper

import com.rkm.tasky.database.model.SyncAttendeeEntity
import com.rkm.tasky.database.model.SyncDeletePhotoEntity
import com.rkm.tasky.database.model.SyncUpdateEventEntity
import com.rkm.tasky.database.model.SyncUpdateEventWithDetails
import com.rkm.tasky.database.model.SyncUploadPhotoEntity
import com.rkm.tasky.network.model.request.UpdateEventRequest
import com.rkm.tasky.sync.model.UpdateEventSyncRequest

fun UpdateEventSyncRequest.asSyncUpdateEventEntity(): SyncUpdateEventEntity {
    return SyncUpdateEventEntity(
        id = this.id,
        title = this.title,
        description = this.description,
        from = this.from,
        to = this.to,
        remindAt = this.remindAt,
        isGoing = this.isGoing
    )
}

fun UpdateEventSyncRequest.asSyncAttendeeEntity(): List<SyncAttendeeEntity> {
    return this.attendees.map { attendee ->
        SyncAttendeeEntity(
            eventId = this.id,
            userId = attendee
        )
    }
}

fun UpdateEventSyncRequest.asSyncUploadPhotosEntity(): List<SyncUploadPhotoEntity> {
    return this.photos.map { photo ->
        SyncUploadPhotoEntity(
            eventId = this.id,
            filePath = photo
        )
    }
}

fun UpdateEventSyncRequest.asSyncDeletePhotoEntity(): List<SyncDeletePhotoEntity> {
    return this.deletedPhotoKeys.map { key ->
        SyncDeletePhotoEntity(
            eventId = this.id,
            photoKey = key
        )
    }
}

fun SyncUpdateEventWithDetails.asUpdateEventRequest(): UpdateEventRequest
{
    return UpdateEventRequest(
        id = this.event.id,
        title = this.event.title,
        description = this.event.description,
        from = this.event.from,
        to = this.event.to,
        remindAt = this.event.remindAt,
        attendeeIds = this.attendees.map { it.userId },
        deletedPhotoKeys = this.photoKeys.map { it.photoKey },
        isGoing = this.event.isGoing
    )
}