package com.rkm.tasky.di.sync

import com.rkm.tasky.sync.manager.abstraction.SyncManager
import com.rkm.tasky.sync.manager.implementation.SyncManagerImpl
import com.rkm.tasky.sync.repository.abstraction.SyncCreateEventRepository
import com.rkm.tasky.sync.repository.abstraction.SyncUpdateEventRepository
import com.rkm.tasky.sync.repository.implementation.SyncCreateEventRepositoryImpl
import com.rkm.tasky.sync.repository.implementation.SyncUpdateEventRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SyncRepositoryModule {

    @Singleton
    @Binds
    abstract fun bindsSyncCreateEventRepository(repository: SyncCreateEventRepositoryImpl): SyncCreateEventRepository

    @Singleton
    @Binds
    abstract fun bindsSyncUpdateEventRepository(repository: SyncUpdateEventRepositoryImpl): SyncUpdateEventRepository

    @Singleton
    @Binds
    abstract fun bindsSyncManager(manager: SyncManagerImpl): SyncManager
}