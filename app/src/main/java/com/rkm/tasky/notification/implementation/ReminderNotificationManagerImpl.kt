package com.rkm.tasky.notification.implementation
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.rkm.tasky.R
import com.rkm.tasky.alarm.model.ReminderAlarm
import com.rkm.tasky.feature.common.AgendaItemType
import com.rkm.tasky.notification.abstraction.ReminderNotificationManager
import com.rkm.tasky.ui.activity.MainActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class ReminderNotificationManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context
): ReminderNotificationManager {
    override fun showReminderNotification(
        alarm: ReminderAlarm
    ) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val notification = NotificationCompat.Builder(context, REMINDER_NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.tasky_logo)
            .setContentTitle(alarm.title)
            .setContentText(alarm.desc)
            .setStyle(NotificationCompat.BigTextStyle().bigText(alarm.desc))
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val manager = context.getSystemService(NotificationManager::class.java)

        manager.notify(alarm.hashCode(), notification)
    }

    companion object {
        const val REMINDER_NOTIFICATION_CHANNEL_ID = "TASKY_REMINDER_CHANNEL_ID"
    }

}
