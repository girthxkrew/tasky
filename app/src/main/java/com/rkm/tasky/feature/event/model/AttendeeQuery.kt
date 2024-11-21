package com.rkm.tasky.feature.event.model

data class AttendeeQuery(
    val doesUserExist: Boolean,
    val attendeeEmail: String,
    val attendeeFullName: String,
    val attendeeUserId: String
)
