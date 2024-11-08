package com.rkm.tasky.feature.agenda.screen.dayselector

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

internal data class DateItem(
    val dayLong: Long,
    val dayOfWeek: String,
    val dayOfMonth: Int,
)

internal fun generateDayItemList(day: Long, timeSpan: TimeSpan): List<DateItem> {
    val timeZone = TimeZone.UTC
    var currentDay = Instant.fromEpochMilliseconds(day)
    val dateList = mutableListOf<DateItem>()
    for (i in 0 until timeSpan.getLength()) {
        val date = currentDay.toLocalDateTime(timeZone)
        dateList.add(
            DateItem(
                currentDay.epochSeconds * 1000,
                date.dayOfWeek.name[0].toString(),
                date.dayOfMonth
            )
        )
        currentDay = currentDay.plus(1, DateTimeUnit.DAY, timeZone)
    }
    return dateList
}

