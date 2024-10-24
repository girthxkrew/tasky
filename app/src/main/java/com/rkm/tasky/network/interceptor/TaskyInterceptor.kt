package com.rkm.tasky.network.interceptor

import com.rkm.tasky.BuildConfig
import com.rkm.tasky.network.authentication.abstraction.AuthenticationManager
import com.rkm.tasky.util.result.Result
import com.rkm.tasky.util.storage.abstraction.SessionStorage
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class TaskyInterceptor @Inject constructor(
    private val manager: AuthenticationManager,
    private val sessionStorage: SessionStorage,
): Interceptor {

    private val apiHeader = "x-api-key"
    private val authHeader = "Authorization"
    private val authKey = "Bearer"

    override fun intercept(chain: Interceptor.Chain): Response {

        //If null, no prior login.
        val authInfo = runBlocking {
            sessionStorage.getSession()
        }

        val request = chain.request()
        val newRequest = request.newBuilder()
            .addHeader(apiHeader, BuildConfig.APIKEY)

        if(authInfo != null) {
            newRequest.addHeader(authHeader, "$authKey ${authInfo.accessToken}")
        }

        val response = chain.proceed(newRequest.build())

        if(response.code == 401) {

            val result = runBlocking {
                manager.getNewAccessToken()
            }

            if (result is Result.Success) {
                val newAuthInfo = runBlocking {
                    sessionStorage.getSession()
                }

                newAuthInfo ?: return response

                val retryRequest = request.newBuilder()
                    .addHeader(apiHeader, BuildConfig.APIKEY)
                    .addHeader(authHeader, "$authKey ${newAuthInfo.accessToken}")

                return chain.proceed(retryRequest.build())
            }
        }

        return response
    }
}