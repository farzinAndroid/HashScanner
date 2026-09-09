package com.hashscanner.hashscanner.data.network

import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File

class ProgressRequestBody(
    private val file: File,
    private val contentType: MediaType?,
    private val onProgress: (bytesWritten: Long, totalBytes: Long, percentage: Int) -> Unit
) : RequestBody() {

    override fun contentType(): MediaType? = contentType

    override fun contentLength(): Long = file.length()

    override fun writeTo(sink: BufferedSink) {
        val totalBytes = file.length()
        if (totalBytes <= 0L) {
            onProgress(0L, 0L, 100)
            return
        }

        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        var uploadedBytes = 0L

        file.inputStream().use { inputStream ->
            var read: Int
            var lastPercent = -1

            while (inputStream.read(buffer).also { read = it } != -1) {
                sink.write(buffer, 0, read)
                uploadedBytes += read

                val percent = ((uploadedBytes.toDouble() / totalBytes.toDouble()) * 100).toInt().coerceIn(0, 100)

                if (percent != lastPercent) {
                    lastPercent = percent
                    onProgress(uploadedBytes, totalBytes, percent)
                }
            }
        }
    }

    companion object {
        private const val DEFAULT_BUFFER_SIZE = 8192
    }
}
