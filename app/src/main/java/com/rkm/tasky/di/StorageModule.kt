package com.rkm.tasky.di

import com.rkm.tasky.util.storage.abstraction.SessionStorage
import com.rkm.tasky.util.storage.implementation.SessionStorageImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class StorageModule {

    @Singleton
    @Binds
    abstract fun bindsSessionStorage(storage: SessionStorageImpl): SessionStorage
}