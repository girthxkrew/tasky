package com.rkm.tasky.notification.abstraction

import android.app.PendingIntent
import com.rkm.tasky.alarm.model.ReminderAlarm
import com.rkm.tasky.feature.common.AgendaItemType

interface ReminderNotificationManager {
    fun showReminderNotification(alarm: ReminderAlarm)
}