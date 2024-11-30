package com.rkm.tasky.database.model

data class EventAttendeesAndPhotosToDelete(
    val attendees: List<AttendeeEntity>,
    val photos: List<PhotoEntity>
)
