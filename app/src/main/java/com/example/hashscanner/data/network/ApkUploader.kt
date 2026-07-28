package com.example.hashscanner.data.network

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Named

class ApkUploader @Inject constructor(

    private val client: OkHttpClient,

    @Named("apk_base_url") private val baseUrl: String

) {

    suspend fun upload(

        apk: File,

        packageName: String

    ): Boolean = withContext(Dispatchers.IO) {

        Log.d("ApkUploader", "Starting APK upload for $packageName to $baseUrl")

        if (baseUrl.isBlank() || !baseUrl.startsWith("http")) {
            Log.e("ApkUploader", "Invalid URL: $baseUrl")
            return@withContext false
        }

        try {

            val body = MultipartBody.Builder()

                .setType(MultipartBody.FORM)

                .addFormDataPart(

                    "package",

                    packageName

                )

                .addFormDataPart(

                    "apk",

                    apk.name,

                    apk.asRequestBody()

                )

                .build()

            val request = Request.Builder()

                .url(baseUrl)

                .post(body)

                .build()

            val response = client

                .newCall(request)

                .execute()

            Log.d("ApkUploader", "Response for $packageName: ${response.code} ${response.message}")
            if (!response.isSuccessful) {
                Log.e("ApkUploader", "Upload failed with body: ${response.body?.string()}")
            }

            response.isSuccessful

        } catch (e: Exception) {

            Log.e("ApkUploader", "Exception during APK upload for $packageName", e)

            false

        }
    }


}

