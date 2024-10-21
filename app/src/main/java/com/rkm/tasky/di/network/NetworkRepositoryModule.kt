package com.rkm.tasky.di.network

import com.rkm.tasky.network.repository.abstraction.TaskyRemoteRepository
import com.rkm.tasky.network.repository.implementation.TaskyRemoteRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkRepositoryModule {

    @Singleton
    @Binds
    abstract fun bindsTaskyRemoteRepository(repositoryImpl: TaskyRemoteRepositoryImpl): TaskyRemoteRepository
}