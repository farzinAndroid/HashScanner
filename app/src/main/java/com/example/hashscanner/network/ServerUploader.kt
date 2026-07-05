package com.example.hashscanner.network

import com.example.hashscanner.database.entity.SuspiciousApp
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

class ServerUploader(

    private val serverUrl: String

) {

    private val client = OkHttpClient()

    fun upload(

        apps: List<SuspiciousApp>

    ): Boolean {

        return try {

            val array = JSONArray()

            apps.forEach {

                val obj = JSONObject()

                obj.put("appName", it.appName)

                obj.put("packageName", it.packageName)

                obj.put("sha256", it.sha256)

                obj.put("riskScore", it.riskScore)

                obj.put("riskLevel", it.riskLevel)

                obj.put("reason", it.reason)

                obj.put("reportDate", it.reportDate)

                array.put(obj)

            }

            val body = JSONObject()

            body.put("apps", array)

            val requestBody =

                body.toString()

                    .toRequestBody(

                        "application/json".toMediaType()

                    )

            val request =

                Request.Builder()

                    .url(serverUrl)

                    .post(requestBody)

                    .build()

            val response =

                client.newCall(request)

                    .execute()

            response.isSuccessful

        } catch (e: Exception) {

            false

        }

    }

}