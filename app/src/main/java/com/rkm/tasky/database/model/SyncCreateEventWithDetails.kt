package com.rkm.tasky.database.model

import androidx.room.Embedded
import androidx.room.Relation

data class SyncCreateEventWithDetails(
    @Embedded val event: SyncCreateEventEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "eventId"
    )
    val attendees: List<SyncAttendeeEntity>,
    @Relation(
        parentColumn = "id",
        entityColumn = "eventId"
    )
    val photos: List<SyncUploadPhotoEntity>
)
