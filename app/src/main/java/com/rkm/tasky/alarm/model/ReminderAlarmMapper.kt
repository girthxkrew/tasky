package com.rkm.tasky.alarm.model

import com.rkm.tasky.database.model.EventEntity
import com.rkm.tasky.database.model.ReminderEntity
import com.rkm.tasky.database.model.TaskEntity
import com.rkm.tasky.feature.common.AgendaItemType
import com.rkm.tasky.feature.event.model.Event
import com.rkm.tasky.feature.reminder.model.Reminder
import com.rkm.tasky.feature.task.model.Task
import com.rkm.tasky.util.date.toLocalDateTime
import com.rkm.tasky.util.date.toLong

fun Reminder.toReminderAlarm(): ReminderAlarm {
    return ReminderAlarm(
        id = this.id,
        title = this.title,
        desc = this.description,
        time = this.remindAt,
        type = AgendaItemType.REMINDER
    )
}

fun ReminderEntity.toReminderAlarm(): ReminderAlarm {
    return ReminderAlarm(
        id = this.id,
        title = this.title,
        desc = this.description,
        time = this.remindAt.toLocalDateTime(),
        type = AgendaItemType.REMINDER
    )
}

fun Task.toReminderAlarm(): ReminderAlarm {
    return ReminderAlarm(
        id = this.id,
        title = this.title,
        desc = this.description,
        time = this.remindAt,
        type = AgendaItemType.TASK
    )
}

fun TaskEntity.toReminderAlarm(): ReminderAlarm {
    return ReminderAlarm(
        id = this.id,
        title = this.title,
        desc = this.description,
        time = this.remindAt.toLocalDateTime(),
        type = AgendaItemType.TASK
    )
}

fun Event.toReminderAlarm(): ReminderAlarm {
    return ReminderAlarm(
        id = this.id,
        title = this.title,
        desc = this.description,
        time = this.remindAt.toLocalDateTime(),
        type = AgendaItemType.EVENT
    ) 
}

fun EventEntity.toReminderAlarm(): ReminderAlarm {
    return ReminderAlarm(
        id = this.id,
        title = this.title,
        desc = this.description,
        time = this.remindAt.toLocalDateTime(),
        type = AgendaItemType.EVENT
    )
}

fun ReminderAlarmParcelable.toReminderAlarm(): ReminderAlarm {
    return ReminderAlarm(
        id = this.id,
        title = this.title,
        desc = this.desc,
        time = this.time.toLocalDateTime(),
        type = AgendaItemType.valueOf(type)
    )
}

fun ReminderAlarm.toParcelable(): ReminderAlarmParcelable {
    return ReminderAlarmParcelable(
        id = this.id,
        title = this.title,
        desc = this.desc,
        time = this.time.toLong(),
        type = this.type.name
    )
}

