package com.rkm.tasky.feature.agenda.screen

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
object DateHelperModule {
    @Provides
    fun providesDateHelper(): DateHelper {
        return DateHelper()
    }
}

class DateHelper @Inject constructor() {

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

    fun getDayNumeric(date: Long): Int {
        val day = Instant.fromEpochMilliseconds(date)
            .toLocalDateTime(timezone)

        return day.dayOfMonth
    }

    fun getSixDaySelectorList(date: Long): List<DayUiModel> {
        println(date)
        val day = Instant.fromEpochMilliseconds(date)
        val list = mutableListOf(DayUiModel(date, getDayName(date)[0].toString(), getDayNumeric(date)))
        var currentDay = day
        repeat(5) {
            currentDay = currentDay.plus(1, DateTimeUnit.DAY, timezone)
            val currentDayLong = currentDay.epochSeconds * 1000
            list.add(DayUiModel(currentDayLong, getDayName(currentDayLong)[0].toString(), getDayNumeric(currentDayLong)))
        }

        return list
    }
}