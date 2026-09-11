package com.hashscanner.hashscanner.data.model.api

import com.google.gson.annotations.SerializedName
import com.hashscanner.hashscanner.data.model.db_entities.SuspiciousApp

data class AppReport(
    @SerializedName("apps")
    val apps: List<App>,
    @SerializedName("deviceId")
    val deviceId: String,
    @SerializedName("scanId")
    val scanId: String
) {
    companion object {
        fun fromSuspiciousApps(
            list: List<SuspiciousApp>,
            deviceId: String,
            scanId: String
        ): AppReport {
            return AppReport(
                apps = list.map { suspicious ->
                    App(
                        appName = suspicious.appName,
                        packageName = suspicious.packageName,
                        reason = suspicious.reason,
                        recommendUpload = suspicious.recommendUpload,
                        recommendation = suspicious.recommendation,
                        reportDate = suspicious.reportDate,
                        riskLevel = suspicious.riskLevel,
                        riskScore = suspicious.riskScore,
                        sha256 = suspicious.sha256
                    )
                },
                deviceId = deviceId,
                scanId = scanId
            )
        }
    }
}
