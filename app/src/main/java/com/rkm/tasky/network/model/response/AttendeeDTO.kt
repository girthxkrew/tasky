package com.rkm.tasky.network.model.response

data class AttendeeDTO(
    val doesUserExist: Boolean,
    val attendee: Attendee
) {
    data class Attendee(
        val email: String,
        val fullName: String,
        val userId: String
    )
}
