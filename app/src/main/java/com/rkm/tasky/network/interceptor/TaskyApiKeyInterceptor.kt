package com.rkm.tasky.network.interceptor

import com.rkm.tasky.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class TaskyApiKeyInterceptor @Inject constructor(): Interceptor {

    private val apiHeader = "x-api-key"

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newRequest = request.newBuilder().addHeader(
            apiHeader, BuildConfig.API_KEY
        )
        return chain.proceed(newRequest.build())
    }
}