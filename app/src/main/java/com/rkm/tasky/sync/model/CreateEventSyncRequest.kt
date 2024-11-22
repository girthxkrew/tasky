package com.rkm.tasky.sync.model

data class CreateEventSyncRequest(
    val id: String,
    val title: String,
    val description: String,
    val from: Long,
    val to: Long,
    val remindAt: Long,
    val attendees: List<String>,
    val photos: List<String>
)
