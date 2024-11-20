package com.rkm.tasky.di.notification

import com.rkm.tasky.notification.abstraction.ReminderNotificationManager
import com.rkm.tasky.notification.implementation.ReminderNotificationManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class NotificationModule {
    @Binds
    abstract fun bindsReminderNotificationManager(manager: ReminderNotificationManagerImpl): ReminderNotificationManager
}