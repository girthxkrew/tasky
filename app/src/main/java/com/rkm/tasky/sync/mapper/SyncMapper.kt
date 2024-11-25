package com.rkm.tasky.sync.mapper

import com.rkm.tasky.database.model.SyncEntity
import com.rkm.tasky.database.model.SyncItemType
import com.rkm.tasky.network.model.request.SyncAgendaRequest

fun List<SyncEntity>.asSyncAgendaRequest(): SyncAgendaRequest {
    return SyncAgendaRequest(
        deletedEventIds = this.filter { it.item == SyncItemType.EVENT }.map { event -> event.itemId },
        deletedTaskIds = this.filter { it.item == SyncItemType.TASK }.map { task -> task.itemId },
        deletedReminderIds = this.filter { it.item == SyncItemType.REMINDER }.map { reminder -> reminder.itemId }
    )
}