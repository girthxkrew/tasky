package com.rkm.tasky

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.rkm.tasky.notification.implementation.ReminderNotificationManagerImpl.Companion.REMINDER_NOTIFICATION_CHANNEL_ID
//import com.rkm.tasky.notification.implementation.ReminderNotificationManagerImpl.Companion.REMINDER_NOTIFICATION_CHANNEL_ID
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class TaskyApplication: Application(), Configuration.Provider {

    @Inject lateinit var workFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = this.getSystemService(NotificationManager::class.java)
            if(manager.getNotificationChannel(REMINDER_NOTIFICATION_CHANNEL_ID) == null) {
                val channel = NotificationChannel(
                    REMINDER_NOTIFICATION_CHANNEL_ID,
                    getString(R.string.notification_channel_name),
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = getString(R.string.notification_channel_desc)
                }
                manager.createNotificationChannel(channel)
            }
        }
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workFactory)
            .build()
}