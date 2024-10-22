package com.rkm.tasky.network.util

import com.google.gson.JsonParseException
import com.rkm.tasky.util.result.Result
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException

/*
 * Error code and exceptions to catch from here: https://medium.com/@dawinderapps/android-interview-questions-45-essential-exceptions-to-catch-in-retrofit-for-reliable-api-calls-957e77529f94
 */

suspend inline fun <reified T> safeCall(execute: () -> Response<T>): Result<T, NetworkError.APIError> {
    val response = try {
        execute()
    } catch (e: IOException) {
        return Result.Error(NetworkError.APIError.NO_INTERNET)
    }catch (e: SocketTimeoutException){
        return Result.Error(NetworkError.APIError.CONNECTION_TIMEOUT)
    } catch (e: JsonParseException) {
        return Result.Error(NetworkError.APIError.SERIALIZATION)
    } catch (e: Exception) {
        return Result.Error(NetworkError.APIError.UNKNOWN)
    }
    return responseToResult(response)
}

suspend inline fun <reified T> responseToResult(response: Response<T>): Result<T, NetworkError.APIError> {
    response.body() ?: return Result.Error(NetworkError.APIError.EMPTY_RESPONSE)
    return when(response.code()) {
        in 200..299 -> Result.Success(response.body()!!)
        401 -> Result.Error(NetworkError.APIError.UNAUTHORIZED)
        403 -> Result.Error(NetworkError.APIError.FORBIDDEN)
        404 -> Result.Error(NetworkError.APIError.NOT_FOUND)
        408 -> Result.Error(NetworkError.APIError.REQUEST_TIMEOUT)
        413 -> Result.Error(NetworkError.APIError.PAYLOAD_TOO_LARGE)
        429 -> Result.Error(NetworkError.APIError.TOO_MANY_REQUESTS)
        in 500..599 -> Result.Error(NetworkError.APIError.SERVER_ERROR)
        else -> Result.Error(NetworkError.APIError.UNKNOWN)
    }
}