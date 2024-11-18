package com.rkm.tasky.alarm.broadcastreceivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.rkm.tasky.alarm.implementation.ReminderAlarmManagerImpl.Companion.REMINDER_ALARM_KEY
import com.rkm.tasky.alarm.model.ReminderAlarm
import com.rkm.tasky.notification.abstraction.ReminderNotificationManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ReminderReceiver: BroadcastReceiver() {

    @Inject lateinit var manager: ReminderNotificationManager

    override fun onReceive(context: Context?, intent: Intent?) {
        var alarm: ReminderAlarm ?= null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            alarm = intent?.getParcelableExtra(REMINDER_ALARM_KEY, ReminderAlarm::class.java)
        } else {
            alarm = intent?.getParcelableExtra<ReminderAlarm>(REMINDER_ALARM_KEY)
        }

        alarm?.let {
            manager.showReminderNotification(it)
        }
    }
}