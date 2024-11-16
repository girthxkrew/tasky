package com.rkm.tasky.util.date

import org.junit.Assert.*

import org.junit.Test

class DateUtilsKtTest {

    @Test
    fun getReminderBeforeDuration() {
        val time = 1732939200000
        val remindAt = 1732917600000
        val result = getReminderBeforeDuration(time, remindAt)
        assertEquals(result, ReminderBeforeDuration.SIX_HOURS)
    }
}