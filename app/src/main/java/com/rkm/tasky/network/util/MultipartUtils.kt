package com.rkm.tasky.network.util

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

fun List<ByteArray>.asMultiPartBody(): List<MultipartBody.Part> {
    return this.mapIndexed { index, bytes ->
        val requestBody = bytes.toRequestBody("image/jpeg".toMediaTypeOrNull())
        MultipartBody.Part.createFormData(
            "photo$index",
            "photo$index.jpg",
            requestBody
        )
    }
}