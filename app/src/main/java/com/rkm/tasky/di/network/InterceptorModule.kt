package com.rkm.tasky.di.network

import com.rkm.tasky.network.interceptor.TaskyInterceptor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class InterceptorModule {

    @Binds
    abstract fun bindsTaskyInterceptor(interceptor: TaskyInterceptor): TaskyInterceptor
}