package com.example.hashscanner.network

import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Named

class ApkUploader @Inject constructor(

    private val client: OkHttpClient,

    @field:Named("base_url") private val baseUrl: String

) {

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

                .url(baseUrl)

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