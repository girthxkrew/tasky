@file:OptIn(FormatStringsInDatetimeFormats::class)

package com.rkm.tasky.util.date

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration


private val timezone = TimeZone.UTC

private val timeFormat = LocalTime.Format {
    amPmHour(padding = Padding.ZERO)
    char(':')
    minute(padding = Padding.ZERO)
    char(' ')
    amPmMarker(am = "AM", pm = "PM")
}
private val dateFormat = LocalDate.Format {
    monthName(names = MonthNames.ENGLISH_ABBREVIATED)
    char(' ')
    dayOfMonth(padding = Padding.ZERO)
    char(' ')
    year(Padding.NONE)
}

fun getMonth(date: Long): String {
    val day = Instant.fromEpochMilliseconds(date)
        .toLocalDateTime(timezone)
    return day.month.name
}

fun getCurrentDayInLong(): Long {
    return Clock.System.todayIn(timezone)
        .atStartOfDayIn(timezone).toEpochMilliseconds()
}

fun getCurrentDayInLocalDateTime(): LocalDateTime {
    return Clock.System.now().toLocalDateTime(timezone)
}

fun getReminderTime(date: Long, unit: ReminderBeforeDuration): Long {
    val reminder = Instant.fromEpochMilliseconds(date)
    return reminder.minus(unit.duration).toEpochMilliseconds()
}

fun Long.toLocalDateTime(): LocalDateTime {
    return Instant.fromEpochMilliseconds(this)
        .toLocalDateTime(timezone)
}

fun LocalDateTime.toLong(): Long {
    return this.toInstant(timezone).toEpochMilliseconds()
}

fun LocalDate.toLong(): Long {
    return this.atStartOfDayIn(timezone).toEpochMilliseconds()
}

fun LocalTime.toUiString(): String {
    return timeFormat.format(this)
}

fun LocalDate.toUiString(): String {
    return dateFormat.format(this)
}

fun getReminderBeforeDuration(time: Long, remindAt: Long): ReminderBeforeDuration {
    val result = Instant.fromEpochMilliseconds(time).minus(Instant.fromEpochMilliseconds(remindAt))
    return result.toReminderBeforeDuration()
}