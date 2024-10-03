package com.rkm.tasky.network.model.response

data class EventResponse(
    val id: String,
    val title: String,
    val description: String,
    val from: Long,
    val to: Long,
    val remindAt: Long,
    val host: String,
    val isUserEventCreator: Boolean,
    val attendees: List<Attendee>,
    val photos: List<Photo>
) {
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
}
