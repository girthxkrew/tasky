package com.rkm.tasky.util.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.net.toUri
import com.rkm.tasky.di.DefaultDispatcher
import com.rkm.tasky.di.IoDispatcher
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import javax.inject.Inject

private const val IMAGE_SIZE_MAX_MB = 1024L * 1024L

class ImageProcessor @Inject constructor(
    @ApplicationContext private val context: Context,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher
) {
    suspend fun getImageFromUri(uri: String): ByteArray?  = withContext(dispatcher){
        if(!doesImageExist(uri)) return@withContext null
        var image: ByteArray ?= null
        try {
            image = getBytes(context, uri.toUri())
        } catch (_: IOException) {
            return@withContext null
        }

        if(getImageSize(uri) > IMAGE_SIZE_MAX_MB) {
            image = image?.let { compressImage(it) }
        }

        return@withContext image
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
        while (outputStream.size() > IMAGE_SIZE_MAX_MB && quality > 10) {
            quality -= 5
            outputStream.reset()
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        }

        return if(outputStream.size() <= IMAGE_SIZE_MAX_MB) {
            outputStream.toByteArray()
        } else {
            null
        }
    }
}