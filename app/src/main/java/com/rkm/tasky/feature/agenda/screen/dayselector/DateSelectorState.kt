package com.rkm.tasky.feature.agenda.screen.dayselector

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
interface DateSelectorState {
    var selectedDate: Long
}

@JvmInline
@Immutable
value class TimeSpan private constructor(private val length: Int) {
    companion object {
        val SIX_DAYS = TimeSpan(6)
        val WEEK = TimeSpan(7)
    }

    internal fun getLength(): Int = length
}