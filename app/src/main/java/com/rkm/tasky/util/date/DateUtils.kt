package com.rkm.tasky.util.date

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import kotlinx.datetime.minus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn

private val timezone = TimeZone.currentSystemDefault()
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
        .atStartOfDayIn(TimeZone.currentSystemDefault()).epochSeconds * 1000
}

fun getCurrentDayInLocalDateTime(): LocalDateTime {
    return Clock.System.now().toLocalDateTime(timezone)
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

fun getReminderTime(date: Long, options: ReminderOptions): Long {
    val reminder = Instant.fromEpochMilliseconds(date)
    return reminder.minus(options.time, options.unit, timezone).toEpochMilliseconds()
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

sealed class ReminderOptions(
    open val time: Int,
    open val unit: DateTimeUnit
) {
    data class TenMinutes(override val time: Int = 10, override val unit: DateTimeUnit = DateTimeUnit.MINUTE): ReminderOptions(time, unit)
    data class ThirtyMinutes(override val time: Int = 30, override val unit: DateTimeUnit = DateTimeUnit.MINUTE): ReminderOptions(time, unit)
    data class OneHour(override val time: Int = 1, override val unit: DateTimeUnit = DateTimeUnit.HOUR): ReminderOptions(time, unit)
    data class SixHour(override val time: Int = 6, override val unit: DateTimeUnit = DateTimeUnit.HOUR): ReminderOptions(time, unit)
    data class OneDay(override val time: Int = 1, override val unit: DateTimeUnit = DateTimeUnit.DAY): ReminderOptions(time, unit)
}