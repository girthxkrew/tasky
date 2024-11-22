package com.rkm.tasky.sync.mapper

import com.rkm.tasky.database.model.SyncAttendeeEntity
import com.rkm.tasky.database.model.SyncCreateEventEntity
import com.rkm.tasky.database.model.SyncUploadPhotoEntity
import com.rkm.tasky.sync.model.CreateEventSyncRequest

fun CreateEventSyncRequest.asSyncCreateEventEntity(): SyncCreateEventEntity {
    return SyncCreateEventEntity(
        id = this.id,
        title = this.title,
        description = this.title,
        from = this.from,
        to = this.to,
        remindAt = this.remindAt
    )
}

fun CreateEventSyncRequest.asSyncAttendeeEntity(): List<SyncAttendeeEntity> {
    return this.attendees.map { attendee ->
        SyncAttendeeEntity(
            eventId = this.id,
            userId = attendee
        )
    }
}

fun CreateEventSyncRequest.asUploadPhotosEntity(): List<SyncUploadPhotoEntity> {
    return this.photos.map { photo ->
        SyncUploadPhotoEntity(
            eventId = this.id,
            filePath = photo
        )
    }
}