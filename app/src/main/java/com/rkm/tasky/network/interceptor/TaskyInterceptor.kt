package com.rkm.tasky.network.interceptor

import com.rkm.tasky.BuildConfig
import com.rkm.tasky.network.authorization.abstraction.AuthorizationManager
import com.rkm.tasky.network.datasource.TaskyRemoteDataSource.RequestType
import com.rkm.tasky.network.datasource.TaskyRemoteDataSource.RequestType.Companion.fromRequest
import com.rkm.tasky.util.result.Result
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class TaskyInterceptor @Inject constructor(
    private val manager: AuthorizationManager,
): Interceptor {

    private val apiHeader = "x-api-key"
    private val authHeader = "Authorization"
    private val authKey = "Bearer"

    override fun intercept(chain: Interceptor.Chain): Response {

        //If null, no prior login.
        val authInfo = runBlocking {
            manager.getSessionInfo()
        }

        val request = chain.request()
        val newRequest = request.newBuilder()
            .addHeader(apiHeader, BuildConfig.API_KEY)

        if(fromRequest(request) == RequestType.AUTHORIZATION && authInfo != null) {
            newRequest.addHeader(authHeader, "$authKey ${authInfo.accessToken}")
        }

        val response = chain.proceed(newRequest.build())

        if(response.code == 401) {

            val result = runBlocking {
                manager.getNewAccessToken()
            }

            if (result is Result.Success) {
                val newAuthInfo = runBlocking {
                   manager.getSessionInfo()
                }

                newAuthInfo ?: return response

                response.close()

                val retryRequest = request.newBuilder()
                    .addHeader(apiHeader, BuildConfig.API_KEY)
                    .addHeader(authHeader, "$authKey ${newAuthInfo.accessToken}")

                return chain.proceed(retryRequest.build())
            }
        }

        return response
    }
}