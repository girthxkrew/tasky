package com.rkm.tasky.database.model

import androidx.room.Embedded
import androidx.room.Relation

data class EventWithDetails(
    @Embedded val event: EventEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "eventId"
    )
    val attendees: List<AttendeeEntity>,

    @Relation(
        parentColumn = "id",
        entityColumn = "eventId"
    )
    val photos: List<PhotoEntity>

)
