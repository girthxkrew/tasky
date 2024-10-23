package com.rkm.tasky.di.network

import com.rkm.tasky.BuildConfig
import com.rkm.tasky.network.datasource.TaskyRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // TODO: Add baseUrl and Api Key to project
    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun providesRemoteDataSource(retrofit: Retrofit): TaskyRemoteDataSource {
        return retrofit.create(TaskyRemoteDataSource::class.java)
    }
}