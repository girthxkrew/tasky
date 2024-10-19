package com.rkm.tasky.di

import com.rkm.tasky.util.security.abstraction.Encryptor
import com.rkm.tasky.util.security.implementation.DataStoreEncryptor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class EncryptorModule {

    @Binds
    abstract fun bindsDataStoreEncryptor(encryptor: DataStoreEncryptor): Encryptor

}