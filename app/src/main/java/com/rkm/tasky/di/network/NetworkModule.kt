package com.rkm.tasky.di.network

import com.rkm.tasky.BuildConfig
import com.rkm.tasky.network.authorization.abstraction.AuthorizationManager
import com.rkm.tasky.network.datasource.TaskyAuthorizationRemoteDateSource
import com.rkm.tasky.network.datasource.TaskyReminderRemoteDataSource
import com.rkm.tasky.network.datasource.TaskyRemoteDataSource
import com.rkm.tasky.network.interceptor.TaskyApiKeyInterceptor
import com.rkm.tasky.network.interceptor.TaskyAuthorizationInterceptor
import dagger.Lazy
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
    fun providesOkHttpClient(
        authInterceptor: TaskyAuthorizationInterceptor,
        apiInterceptor: TaskyApiKeyInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(apiInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun providesRemoteDataSource(retrofit: Retrofit): TaskyRemoteDataSource {
        return retrofit.create(TaskyRemoteDataSource::class.java)
    }

    @Singleton
    @Provides
    fun providesTaskyAuthorizationRemoteDataSource(retrofit: Retrofit): TaskyAuthorizationRemoteDateSource {
        return retrofit.create(TaskyAuthorizationRemoteDateSource::class.java)
    }

    @Singleton
    @Provides
    fun providesTaskReminderRemoteDataSource(retrofit: Retrofit): TaskyReminderRemoteDataSource {
        return retrofit.create(TaskyReminderRemoteDataSource::class.java)
    }

    @Provides
    fun providesTaskyAuthorizationInterceptor(manager: Lazy<AuthorizationManager>): TaskyAuthorizationInterceptor {
        return TaskyAuthorizationInterceptor(manager)
    }

    @Provides
    fun providesTaskyApiKeyInterceptor(): TaskyApiKeyInterceptor {
        return TaskyApiKeyInterceptor()
    }
}