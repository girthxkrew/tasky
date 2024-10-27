package com.rkm.tasky.di.network

import com.rkm.tasky.network.authentication.abstraction.AuthenticationManager
import com.rkm.tasky.network.authentication.implementation.AuthenticationManagerImpl
import com.rkm.tasky.network.repository.abstraction.AuthenticationRepository
import com.rkm.tasky.network.repository.implementation.AuthenticationRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class NetworkVMScopedRepositoryModule {

    @ViewModelScoped
    @Binds
    abstract fun bindsAuthenticationRepository(repository: AuthenticationRepositoryImpl): AuthenticationRepository

}