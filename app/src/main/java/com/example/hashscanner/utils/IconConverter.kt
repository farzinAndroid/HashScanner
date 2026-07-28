package com.example.hashscanner.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream

object IconConverter {

    /**
     * Converts a Bitmap to a ByteArray, downscaling it to 128x128 and encoding as PNG.
     */
    fun bitmapToByteArray(bitmap: Bitmap?): ByteArray? {
        if (bitmap == null) return null
        return try {
            val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 128, 128, true)
            val outputStream = ByteArrayOutputStream()
            scaledBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.toByteArray()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Converts a ByteArray back to a Bitmap safely.
     */
    fun byteArrayToBitmap(data: ByteArray?): Bitmap? {
        if (data == null || data.isEmpty()) return null
        return try {
            BitmapFactory.decodeByteArray(data, 0, data.size)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
