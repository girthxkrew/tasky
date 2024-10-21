package com.rkm.tasky.di.util.security

import com.rkm.tasky.util.security.abstraction.Encryptor
import com.rkm.tasky.util.security.implementation.DataStoreEncryptor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class EncryptorModule {

    @Binds
    abstract fun bindsDataStoreEncryptor(encryptor: DataStoreEncryptor): Encryptor

}