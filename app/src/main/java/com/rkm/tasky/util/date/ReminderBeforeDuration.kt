package com.rkm.tasky.util.date

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