package com.rkm.tasky.alarm.model

import com.rkm.tasky.feature.common.AgendaItemType
import kotlinx.datetime.LocalDateTime

data class ReminderAlarm(
    val id: String,
    val title: String,
    val desc: String? = null,
    val time: LocalDateTime,
    val type: AgendaItemType
)
