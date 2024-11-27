package com.rkm.tasky.di.alarm

import com.rkm.tasky.alarm.abstraction.ReminderAlarmManager
import com.rkm.tasky.alarm.implementation.ReminderAlarmManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AlarmModule {

    @Singleton
    @Binds
    abstract fun bindsReminderAlarmManager(manager: ReminderAlarmManagerImpl): ReminderAlarmManager
}