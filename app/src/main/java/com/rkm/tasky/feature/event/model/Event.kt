package com.rkm.tasky.feature.event.model

data class Event(
    val id: String,
    val title: String,
    val description: String,
    val from: Long,
    val to: Long,
    val remindAt: Long,
    val host: String,
    val isUserEventCreator: Boolean,
    val isUserGoing: Boolean = false,
    val attendees: List<Attendee>,
    val attendeesGoing: List<String> = emptyList(),
    val photos: List<Photo>,
    val deletedPhotoKeys: List<String> = emptyList(),
    val photosToUpload: List<ByteArray> = emptyList()
)
data class Attendee(
    val email: String,
    val fullName: String,
    val userId: String,
    val eventId: String,
    val isGoing: Boolean,
    val remindAt: Long
)
data class Photo(
    val key: String,
    val url: String
)
