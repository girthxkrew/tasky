package com.rkm.tasky.util.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.net.toUri
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException

private const val IMAGE_THRESHOLD = 1024L * 1024L

fun getImageFromUri(context: Context, uri: String): ByteArray? {
    if(!doesImageExist(uri)) return null
    var image: ByteArray ?= null
    try {
        image = getBytes(context, uri.toUri())
    } catch (_: IOException) {
        return null
    }

    if(getImageSize(uri) > IMAGE_THRESHOLD) {
        image = image?.let { compressImage(it) }
    }

    return image
}

@Throws(IOException::class)
private fun getBytes(context: Context, uri: Uri): ByteArray? =
    context.contentResolver.openInputStream(uri)?.use { it.buffered().readBytes() }


private fun doesImageExist(filePath: String): Boolean {
    val file = File(filePath)
    return file.exists() && file.isFile
}

private fun getImageSize(filePath: String): Long {
    val file = File(filePath)
    return if(file.exists()) (file.length()) else 0L
}

private fun compressImage(byteArray: ByteArray): ByteArray? {
    val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    val outputStream = ByteArrayOutputStream()
    var quality = 100
    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
    while (outputStream.size() > IMAGE_THRESHOLD && quality > 10) {
        quality -= 5
        outputStream.reset()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
    }

    return if(outputStream.size() <= IMAGE_THRESHOLD) {
        outputStream.toByteArray()
    } else {
        null
    }
}