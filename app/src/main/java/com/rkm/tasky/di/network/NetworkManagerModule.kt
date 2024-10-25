package com.rkm.tasky.di.network

import com.rkm.tasky.network.authentication.abstraction.AuthenticationManager
import com.rkm.tasky.network.authentication.implementation.AuthenticationManagerImpl
import com.rkm.tasky.network.authorization.abstraction.AuthorizationManager
import com.rkm.tasky.network.authorization.implementation.AuthorizationManagerImpl
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
    abstract fun bindsAuthenticationManager(manager: AuthenticationManagerImpl): AuthenticationManager

    @Singleton
    @Binds
    abstract fun bindsAuthorizationManager(manager: AuthorizationManagerImpl): AuthorizationManager
}