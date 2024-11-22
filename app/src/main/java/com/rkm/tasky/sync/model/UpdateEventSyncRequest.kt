package com.rkm.tasky.sync.model

data class UpdateEventSyncRequest(
    val id: String,
    val title: String,
    val description: String,
    val from: Long,
    val to: Long,
    val remindAt: Long,
    val attendees: List<String>,
    val deletedPhotoKeys: List<String>,
    val photos: List<String>,
    val isGoing: Boolean
)
