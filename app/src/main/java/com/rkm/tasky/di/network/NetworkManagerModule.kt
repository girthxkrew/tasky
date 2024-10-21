package com.rkm.tasky.di.network

import com.rkm.tasky.network.authentication.abstraction.LoginManager
import com.rkm.tasky.network.authentication.implementation.LoginManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkManagerModule {

    @Singleton
    @Binds
    abstract fun bindsLoginManager(manager: LoginManagerImpl): LoginManager
}