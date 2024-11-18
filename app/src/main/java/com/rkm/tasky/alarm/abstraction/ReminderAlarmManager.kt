package com.rkm.tasky.alarm.abstraction

import com.rkm.tasky.alarm.model.ReminderAlarm

interface ReminderAlarmManager {
    fun scheduleReminder(alarms: List<ReminderAlarm>)
    fun cancelReminder(alarms: List<ReminderAlarm>)
}