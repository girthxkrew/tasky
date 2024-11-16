package com.rkm.tasky.util.date

import com.rkm.tasky.util.date.ReminderBeforeDuration.entries
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes


enum class ReminderBeforeDuration(val duration: Duration){
    TEN_MINUTES(10.minutes),
    THIRTY_MINUTES(30.minutes),
    ONE_HOUR(1.hours),
    SIX_HOURS(6.hours),
    ONE_DAY(1.days)
}

fun Duration.toReminderBeforeDuration(): ReminderBeforeDuration {
    return when(this) {
        10.minutes -> ReminderBeforeDuration.TEN_MINUTES
        30.minutes -> ReminderBeforeDuration.THIRTY_MINUTES
        1.hours -> ReminderBeforeDuration.ONE_HOUR
        6.hours -> ReminderBeforeDuration.SIX_HOURS
        1.days -> ReminderBeforeDuration.ONE_HOUR
        else -> ReminderBeforeDuration.TEN_MINUTES
    }
}