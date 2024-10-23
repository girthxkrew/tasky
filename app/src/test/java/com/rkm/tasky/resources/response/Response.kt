package com.rkm.tasky.resources.response

import com.google.gson.Gson
import com.rkm.tasky.network.model.response.AccessTokenResponse
import com.rkm.tasky.network.model.response.LoginUserResponse
import java.io.File

private const val errorMessage = "src/test/java/com/rkm/tasky/resources/response/ErrorMessage.json"
private const val getNewAccessTokenResponse = "src/test/java/com/rkm/tasky/resources/response/GetNewAccessTokenResponse.json"
private const val loginUserResponse = "src/test/java/com/rkm/tasky/resources/response/LoginResponse.json"

private val gson = Gson()

fun loginUserResponseToPojo(): LoginUserResponse {
    val string = File(loginUserResponse).readText()
    return gson.fromJson(string, LoginUserResponse::class.java)
}

fun loginUserResponseToString(): String {
    return File(loginUserResponse).readText()
}

fun getNewAccessTokenResponseToPojo(): AccessTokenResponse {
    val string = File(getNewAccessTokenResponse).readText()
    return gson.fromJson(string, AccessTokenResponse::class.java)
}

fun getNewAccessTokenResponseToString(): String {
    return File(getNewAccessTokenResponse).readText()
}


fun errorMessageToString(): String {
    return File(errorMessage).absoluteFile.readText()
}