package com.rkm.tasky.alarm.implementation

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.rkm.tasky.alarm.abstraction.ReminderAlarmManager
import com.rkm.tasky.alarm.broadcastreceivers.ReminderReceiver
import com.rkm.tasky.alarm.model.ReminderAlarm
import com.rkm.tasky.alarm.model.toParcelable
import com.rkm.tasky.util.date.toLong
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ReminderAlarmManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context
): ReminderAlarmManager {
    private val manager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    override fun scheduleReminder(alarms: List<ReminderAlarm>) {
        alarms.forEach {
            val intent = Intent(context, ReminderReceiver::class.java).apply {
                putExtra(REMINDER_ALARM_KEY, it.toParcelable())
            }

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                REMINDER_REQUEST_CODE,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            manager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                it.time.toLong(),
                pendingIntent
            )
        }
    }

    override fun cancelReminder(alarms: List<ReminderAlarm>) {
        alarms.forEach {
            val intent = Intent(context, ReminderReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                REMINDER_REQUEST_CODE,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_NO_CREATE
            )

            pendingIntent?.let {
                manager.cancel(it)
                it.cancel()
            }
        }
    }

    companion object {
        const val REMINDER_ALARM_KEY = "REMINDER_ALARM_KEY"
        const val REMINDER_REQUEST_CODE = 72
    }
}