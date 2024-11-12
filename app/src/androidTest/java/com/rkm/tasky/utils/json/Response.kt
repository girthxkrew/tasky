package com.rkm.tasky.utils.json

import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import com.rkm.tasky.network.model.response.ReminderDTO
import java.io.BufferedReader
import java.io.InputStreamReader

private const val errorMessage = "ErrorMessage.json"
private const val reminderResponse = "ReminderResponse.json"

private val gson = Gson()

fun reminderResponseToPojo(): ReminderDTO {
    return gson.fromJson(loadJsonFromAsset(reminderResponse), ReminderDTO::class.java)
}

fun reminderResponseToString(): String {
    return loadJsonFromAsset(reminderResponse)
}

fun errorMessageToString(): String {
    return loadJsonFromAsset(errorMessage)
}

private fun loadJsonFromAsset(filename: String): String {
    val context = InstrumentationRegistry.getInstrumentation().context
    val inputStream = context.assets.open(filename)
    val reader = BufferedReader(InputStreamReader(inputStream))
    val content = reader.use { it.readText() }
    reader.close()
    return content
}