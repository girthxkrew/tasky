package com.rkm.tasky.feature.common

import androidx.annotation.StringRes
import com.rkm.tasky.R

enum class ScreenType() {
    EVENT,
    TASK,
    REMINDER
}

fun ScreenType.toUiString(): Int {
    return when(this) {
        ScreenType.EVENT -> R.string.event
        ScreenType.TASK -> R.string.task
        ScreenType.REMINDER -> R.string.reminder
    }
}