package com.rkm.tasky.repository.mapper

import com.rkm.tasky.database.model.AttendeeEntity
import com.rkm.tasky.database.model.EventEntity
import com.rkm.tasky.database.model.EventWithDetails
import com.rkm.tasky.database.model.PhotoEntity
import com.rkm.tasky.feature.event.model.Attendee
import com.rkm.tasky.feature.event.model.Event
import com.rkm.tasky.feature.event.model.Photo
import com.rkm.tasky.network.model.request.CreateEventRequest
import com.rkm.tasky.network.model.request.UpdateEventRequest
import com.rkm.tasky.network.model.response.EventDTO
import com.rkm.tasky.sync.model.CreateEventSyncRequest
import com.rkm.tasky.sync.model.UpdateEventSyncRequest

fun EventWithDetails.toEvent(): Event {
    return Event(
        id = this.event.id,
        title = this.event.title,
        description = this.event.description,
        from = this.event.from,
        to = this.event.to,
        remindAt = this.event.remindAt,
        host = this.event.host,
        isUserEventCreator = this.event.isUserEventCreator,
        attendees = this.attendees.map { it.asAttendee() },
        photos = this.photos.map { it.asPhoto() },
    )
}

fun Event.asCreateEventRequest(): CreateEventRequest {
    return CreateEventRequest(
        id = this.id,
        title = this.title,
        description = this.description,
        from = this.from,
        to = this.to,
        remindAt = this.remindAt,
        attendeeIds = this.attendees.map { it.userId }
    )
}

fun Event.asUpdateEventRequest(): UpdateEventRequest {
    return UpdateEventRequest(
        id = this.id,
        title = this.title,
        description = this.description,
        from = this.from,
        to = this.to,
        remindAt = this.remindAt,
        attendeeIds = this.attendees.map { it.userId },
        deletedPhotoKeys = this.deletedPhotoKeys,
        isGoing = this.isUserGoing
    )
}

fun Event.asEventEntity(): EventEntity{
    return EventEntity(
        id = this.id,
        title = this.title,
        description = this.description,
        from = this.from,
        to = this.to,
        remindAt = this.remindAt,
        host = this.host,
        isUserEventCreator = this.isUserEventCreator
    )
}

fun EventDTO.asEventEntity(): EventEntity {
    return EventEntity(
        id = this.id,
        title = this.title,
        description = this.description,
        from = this.from,
        to = this.to,
        remindAt = this.remindAt,
        host = this.host,
        isUserEventCreator = this.isUserEventCreator
    )
}

fun AttendeeEntity.asAttendee(): Attendee {
    return Attendee(
        email = this.email,
        fullName = this.fullName,
        userId = this.userId,
        eventId = this.eventId,
        isGoing = this.isGoing,
        remindAt = this.remindAt
    )
}

fun PhotoEntity.asPhoto(): Photo {
    return Photo(
        key = this.key,
        url = this.url
    )
}

fun EventDTO.Attendee.asAttendeeEntity(): AttendeeEntity {
    return AttendeeEntity(
        userId = this.userId,
        email = this.email,
        fullName = this.fullName,
        eventId = this.eventId,
        isGoing = this.isGoing,
        remindAt = this.remindAt
    )
}

fun EventDTO.asEventWithDetails(): EventWithDetails {
    return EventWithDetails(
        event = this.asEventEntity(),
        attendees = this.attendees.map { it.asAttendeeEntity() },
        photos = this.photos.map { it.asPhotoEntity(this.id) }
    )
}

fun EventDTO.Photo.asPhotoEntity(id: String): PhotoEntity {
    return PhotoEntity(
        key = this.key,
        url = this.url,
        eventId = id
    )
}

fun Event.asSyncCreateEventRequest(): CreateEventSyncRequest {
    return CreateEventSyncRequest(
        id = this.id,
        title = this.title,
        description = this.description,
        from = this.from,
        to = this.to,
        remindAt = this.remindAt,
        attendees = this.attendees.map { attendee -> attendee.userId },
        photos = this.photosToUploadFilePaths
    )
}

fun Event.asSyncUpdateEventRequest(): UpdateEventSyncRequest {
    return UpdateEventSyncRequest(
        id = this.id,
        title = this.title,
        description = this.description,
        from = this.from,
        to = this.to,
        remindAt = this.remindAt,
        attendees = this.attendees.map { attendee -> attendee.userId },
        deletedPhotoKeys = this.deletedPhotoKeys,
        isGoing = this.isUserGoing,
        photos = this.photosToUploadFilePaths
    )
}