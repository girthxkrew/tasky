package com.rkm.tasky.network.interceptor

import RequestType.Companion.fromRequest
import com.rkm.tasky.network.authorization.abstraction.AuthorizationManager
import com.rkm.tasky.util.result.Result
import dagger.Lazy
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class TaskyAuthorizationInterceptor @Inject constructor(
    private val lazyManager: Lazy<AuthorizationManager>,
): Interceptor {

    private val authHeader = "Authorization"
    private val authKey = "Bearer"
    private lateinit var manager: AuthorizationManager

    override fun intercept(chain: Interceptor.Chain): Response {

        manager = lazyManager.get()

        val request = chain.request()

        if(fromRequest(request) == RequestType.AUTHENTICATION) {
            return chain.proceed(request)
        }

        val authInfo = runBlocking { manager.getSessionInfo() }

        authInfo ?: return chain.proceed(request)

        val newRequest = request.newBuilder()
            .addHeader(authHeader, "$authKey ${authInfo.accessToken}")

        val response = chain.proceed(newRequest.build())

        if(response.code == 401) {

            val result = runBlocking {
                manager.getNewAccessToken()
            }

            if (result is Result.Success) {

                val newAuthInfo = runBlocking { manager.getSessionInfo() }

                newAuthInfo ?: return response

                response.close()

                val retryRequest = request.newBuilder()
                    .addHeader(authHeader, "$authKey ${newAuthInfo.accessToken}")

                return chain.proceed(retryRequest.build())
            }
        }

        return response
    }

}