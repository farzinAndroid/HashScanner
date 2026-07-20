package com.example.hashscanner.network

import com.example.hashscanner.data.model.db_entities.SuspiciousApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Named

class ServerUploader @Inject constructor(

    private val client: OkHttpClient,

    @Named("base_url") private val baseUrl: String

) {

    suspend fun upload(

        app: SuspiciousApp,

        deviceId: String

    ): Boolean = withContext(Dispatchers.IO) {

        if (baseUrl.isBlank() || !baseUrl.startsWith("http")) {
            return@withContext false
        }

        try {

            val json = JSONObject().apply {

                put("deviceId", deviceId)

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

                .url(baseUrl)

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