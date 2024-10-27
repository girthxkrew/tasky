package com.rkm.tasky.di.network

import com.rkm.tasky.network.authentication.abstraction.AuthenticationManager
import com.rkm.tasky.network.authentication.implementation.AuthenticationManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class NetworkVMScopedManagerModule {

    @ViewModelScoped
    @Binds
    abstract fun bindsAuthenticationManager(manager: AuthenticationManagerImpl): AuthenticationManager

}