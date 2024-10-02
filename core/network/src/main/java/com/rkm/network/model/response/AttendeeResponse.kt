package com.rkm.network.model.response

data class AttendeeResponse(
    val doesUserExist: Boolean,
    val attendee: Attendee
) {
    data class Attendee(
        val email: String,
        val fullName: String,
        val userId: String
    )
}
