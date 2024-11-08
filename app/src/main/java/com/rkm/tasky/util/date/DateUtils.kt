package com.rkm.tasky.util.date

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn

private val timezone = TimeZone.currentSystemDefault()

fun getMonth(date: Long): String {
    val day = Instant.fromEpochMilliseconds(date)
        .toLocalDateTime(timezone)
    return day.month.name
}

fun getCurrentDay(): Long {
    return Clock.System.todayIn(timezone)
        .atStartOfDayIn(TimeZone.currentSystemDefault()).epochSeconds * 1000
}

fun getDayName(date: Long): String {
    val day = Instant.fromEpochMilliseconds(date)
        .toLocalDateTime(timezone)

    return day.dayOfWeek.name
}

fun getDayOfTheMonth(date: Long): Int {
    val day = Instant.fromEpochMilliseconds(date)
        .toLocalDateTime(timezone)

    return day.dayOfMonth
}