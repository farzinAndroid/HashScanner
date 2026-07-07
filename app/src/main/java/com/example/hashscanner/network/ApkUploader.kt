package com.example.hashscanner.network

import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class ApkUploader(

    private val serverUrl: String

) {

    private val client = OkHttpClient()

    fun upload(

        apk: File,

        packageName: String

    ): Boolean {

        return try {

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

                .url(serverUrl)

                .post(body)

                .build()

            val response = client

                .newCall(request)

                .execute()

            response.isSuccessful

        } catch (e: Exception) {

            e.printStackTrace()

            false

        }

    }

}