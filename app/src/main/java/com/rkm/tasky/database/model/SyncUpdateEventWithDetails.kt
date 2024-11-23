package com.rkm.tasky.database.model

import androidx.room.Embedded
import androidx.room.Relation

data class SyncUpdateEventWithDetails(
    @Embedded val event: SyncUpdateEventEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "eventId"
    )
    val uploadPhotos: List<SyncUploadPhotoEntity>,
    @Relation(
        parentColumn = "id",
        entityColumn = "eventId"
    )
    val photoKeys: List<SyncDeletePhotoEntity>,
    @Relation(
        parentColumn = "id",
        entityColumn = "eventId"
    )
    val attendees: List<SyncAttendeeEntity>
)
