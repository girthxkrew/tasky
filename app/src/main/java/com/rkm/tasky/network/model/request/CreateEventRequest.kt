package com.rkm.tasky.network.model.request

import java.util.UUID

data class CreateEventRequest(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    val from: Long,
    val to: Long,
    val remindAt: Long,
    val attendeeIds: List<String>
)
