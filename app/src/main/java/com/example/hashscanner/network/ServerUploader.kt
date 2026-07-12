package com.example.hashscanner.network

import com.example.hashscanner.data.model.db_entities.SuspiciousApp
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class ServerUploader(

    private val serverUrl: String

) {

    private val client = OkHttpClient()

    suspend fun upload(

        app: SuspiciousApp

    ): Boolean {

        return try {

            val json = JSONObject().apply {

                put("appName", app.appName)

                put("packageName", app.packageName)

                put("sha256", app.sha256)

                put("riskScore", app.riskScore)

                put("riskLevel", app.riskLevel)

                put("reason", app.reason)

                put("reportDate", app.reportDate)

                put("recommendUpload", app.recommendUpload)

                put("recommendation", app.recommendation)

            }

            val body = json.toString().toRequestBody(

                "application/json".toMediaType()

            )

            val request = Request.Builder()

                .url(serverUrl)

                .post(body)

                .build()

            client.newCall(request)

                .execute()

                .use { response ->

                    response.isSuccessful

                }

        } catch (e: Exception) {

            e.printStackTrace()

            false

        }

    }

}