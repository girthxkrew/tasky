package com.rkm.tasky.di.network

import com.rkm.tasky.di.IoDispatcher
import com.rkm.tasky.network.authentication.abstraction.AuthenticationManager
import com.rkm.tasky.network.authentication.implementation.AuthenticationManagerImpl
import com.rkm.tasky.network.authorization.abstraction.AuthorizationManager
import com.rkm.tasky.network.authorization.implementation.AuthorizationManagerImpl
import com.rkm.tasky.network.repository.abstraction.AuthorizationRepository
import com.rkm.tasky.util.storage.abstraction.SessionStorage
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkManagerModule {

    @Singleton
    @Binds
    abstract fun bindsAuthorizationManager(manager: AuthorizationManagerImpl): AuthorizationManager
}