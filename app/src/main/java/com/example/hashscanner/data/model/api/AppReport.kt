package com.example.hashscanner.data.model.api

import com.example.hashscanner.data.model.db_entities.SuspiciousApp
import com.google.gson.annotations.SerializedName

data class AppReport(
    @SerializedName("deviceId")
    val deviceId: String,
    @SerializedName("appName")
    val appName: String,
    @SerializedName("packageName")
    val packageName: String,
    @SerializedName("sha256")
    val sha256: String,
    @SerializedName("riskScore")
    val riskScore: Int,
    @SerializedName("riskLevel")
    val riskLevel: String,
    @SerializedName("reason")
    val reason: String,
    @SerializedName("reportDate")
    val reportDate: String,
    @SerializedName("recommendUpload")
    val recommendUpload: Boolean,
    @SerializedName("recommendation")
    val recommendation: String
) {
    companion object {
        fun fromSuspiciousApp(app: SuspiciousApp, deviceId: String): AppReport {
            return AppReport(
                deviceId = deviceId,
                appName = app.appName,
                packageName = app.packageName,
                sha256 = app.sha256,
                riskScore = app.riskScore,
                riskLevel = app.riskLevel,
                reason = app.reason,
                reportDate = app.reportDate,
                recommendUpload = app.recommendUpload,
                recommendation = app.recommendation
            )
        }
    }
}
