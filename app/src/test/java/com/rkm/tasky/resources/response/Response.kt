package com.rkm.tasky.resources.response

import com.google.gson.Gson
import com.rkm.tasky.network.model.response.AccessTokenDTO
import com.rkm.tasky.network.model.response.LoginUserDTO
import java.io.File

private const val errorMessage = "src/test/java/com/rkm/tasky/resources/response/ErrorMessage.json"
private const val getNewAccessTokenResponse = "src/test/java/com/rkm/tasky/resources/response/GetNewAccessTokenResponse.json"
private const val loginUserResponse = "src/test/java/com/rkm/tasky/resources/response/LoginResponse.json"

private val gson = Gson()

fun loginUserResponseToPojo(): LoginUserDTO {
    val string = File(loginUserResponse).readText()
    return gson.fromJson(string, LoginUserDTO::class.java)
}

fun loginUserResponseToString(): String {
    return File(loginUserResponse).readText()
}

fun getNewAccessTokenResponseToPojo(): AccessTokenDTO {
    val string = File(getNewAccessTokenResponse).readText()
    return gson.fromJson(string, AccessTokenDTO::class.java)
}

fun getNewAccessTokenResponseToString(): String {
    return File(getNewAccessTokenResponse).readText()
}


fun errorMessageToString(): String {
    return File(errorMessage).absoluteFile.readText()
}