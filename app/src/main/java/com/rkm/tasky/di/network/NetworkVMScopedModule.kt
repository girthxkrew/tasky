package com.rkm.tasky.di.network

import com.rkm.tasky.network.datasource.TaskyAuthenticationRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
object NetworkVMScopedModule {

    @ViewModelScoped
    @Provides
    fun providesTaskyAuthenticationRemoteDataSource(retrofit: Retrofit): TaskyAuthenticationRemoteDataSource {
        return retrofit.create(TaskyAuthenticationRemoteDataSource::class.java)
    }
}