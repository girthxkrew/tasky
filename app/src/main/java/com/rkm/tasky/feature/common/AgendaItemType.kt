package com.rkm.tasky.feature.common

import com.rkm.tasky.R

enum class AgendaItemType {
    EVENT,
    TASK,
    REMINDER
}

fun AgendaItemType.toUiString(): Int {
    return when(this) {
        AgendaItemType.EVENT -> R.string.event
        AgendaItemType.TASK -> R.string.task
        AgendaItemType.REMINDER -> R.string.reminder
    }
}