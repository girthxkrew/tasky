package com.rkm.tasky.alarm.broadcastreceivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.rkm.tasky.alarm.implementation.ReminderAlarmManagerImpl.Companion.REMINDER_ALARM_KEY
import com.rkm.tasky.alarm.model.ReminderAlarmParcelable
import com.rkm.tasky.alarm.model.toReminderAlarm
import com.rkm.tasky.notification.abstraction.ReminderNotificationManager
import com.rkm.tasky.util.intent.getCompatParcelableExtra
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ReminderReceiver: BroadcastReceiver() {

    @Inject lateinit var manager: ReminderNotificationManager

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.getCompatParcelableExtra<ReminderAlarmParcelable>(REMINDER_ALARM_KEY)?.let {
            manager.showReminderNotification(it.toReminderAlarm())
        }
    }
}