package com.rkm.tasky.di.network

import com.rkm.tasky.BuildConfig
import com.rkm.tasky.network.datasource.TaskyRemoteDataSource
import com.rkm.tasky.network.interceptor.TaskyInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // TODO: Add baseUrl and Api Key to project
    @Singleton
    @Provides
    fun providesRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun providesOkHttpClient(interceptor: TaskyInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    @Singleton
    @Provides
    fun providesRemoteDataSource(retrofit: Retrofit): TaskyRemoteDataSource {
        return retrofit.create(TaskyRemoteDataSource::class.java)
    }
}