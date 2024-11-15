package com.rkm.tasky.di.repository

import com.rkm.tasky.repository.abstraction.TaskyReminderRepository
import com.rkm.tasky.repository.abstraction.TaskyTaskRepository
import com.rkm.tasky.repository.implementation.TaskyReminderRepositoryImpl
import com.rkm.tasky.repository.implementation.TaskyTaskRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun providesTaskyReminderRepository(repository: TaskyReminderRepositoryImpl): TaskyReminderRepository

    @Singleton
    @Binds
    abstract fun providesTaskyTaskRepository(repository: TaskyTaskRepositoryImpl): TaskyTaskRepository
}