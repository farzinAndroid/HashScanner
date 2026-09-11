package com.hashscanner.hashscanner.data.model.api

import com.google.gson.annotations.SerializedName

data class ScanResultResponse(
    @SerializedName("ready")
    val ready: Boolean,
    @SerializedName("stage")
    val stage: String? = null,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("totalReports")
    val totalReports: Int? = null,
    @SerializedName("checkedReports")
    val checkedReports: Int? = null,
    @SerializedName("pendingReports")
    val pendingReports: Int? = null,
    @SerializedName("initialReport")
    val initialReport: InitialReport? = null,
    @SerializedName("uploadedApks")
    val uploadedApks: UploadedApks? = null,
    @SerializedName("finalReport")
    val finalReport: FinalReport? = null
)

data class InitialReport(
    @SerializedName("complete")
    val complete: Boolean,
    @SerializedName("summary")
    val summary: ReportSummary,
    @SerializedName("apps")
    val apps: List<ApiAppResult>
)

data class ReportSummary(
    @SerializedName("total")
    val total: Int = 0,
    @SerializedName("virus")
    val virus: Int = 0,
    @SerializedName("suspicious")
    val suspicious: Int = 0,
    @SerializedName("safe")
    val safe: Int = 0,
    @SerializedName("virusApps")
    val virusApps: List<String> = emptyList(),
    @SerializedName("suspiciousApps")
    val suspiciousApps: List<String> = emptyList(),
    @SerializedName("safeApps")
    val safeApps: List<String> = emptyList()
)

data class UploadedApks(
    @SerializedName("summary")
    val summary: UploadSummary,
    @SerializedName("apps")
    val apps: List<ApiAppResult> = emptyList()
)

data class UploadSummary(
    @SerializedName("total")
    val total: Int = 0,
    @SerializedName("checked")
    val checked: Int = 0,
    @SerializedName("pending")
    val pending: Int = 0,
    @SerializedName("virus")
    val virus: Int = 0,
    @SerializedName("safe")
    val safe: Int = 0,
    @SerializedName("suspicious")
    val suspicious: Int = 0,
    @SerializedName("virusApps")
    val virusApps: List<String> = emptyList(),
    @SerializedName("safeApps")
    val safeApps: List<String> = emptyList(),
    @SerializedName("suspiciousApps")
    val suspiciousApps: List<String> = emptyList(),
    @SerializedName("pendingApps")
    val pendingApps: List<String> = emptyList(),
    @SerializedName("complete")
    val complete: Boolean = false
)

data class FinalReport(
    @SerializedName("complete")
    val complete: Boolean = false,
    @SerializedName("summary")
    val summary: FinalSummary,
    @SerializedName("message")
    val message: String
)

data class FinalSummary(
    @SerializedName("total")
    val total: Int = 0,
    @SerializedName("virus")
    val virus: Int = 0,
    @SerializedName("safe")
    val safe: Int = 0,
    @SerializedName("pending")
    val pending: Int = 0,
    @SerializedName("virusApps")
    val virusApps: List<String> = emptyList(),
    @SerializedName("safeApps")
    val safeApps: List<String> = emptyList(),
    @SerializedName("pendingApps")
    val pendingApps: List<String> = emptyList()
)

data class ApiAppResult(
    @SerializedName("appName")
    val appName: String = "",
    @SerializedName("packageName")
    val packageName: String = "",
    @SerializedName("sha256")
    val sha256: String = "",
    @SerializedName("riskScore")
    val riskScore: Int = 0,
    @SerializedName("riskLevel")
    val riskLevel: String = "",
    @SerializedName("reason")
    val reason: String = "",
    @SerializedName("result")
    val result: String = "",
    @SerializedName("action")
    val action: String = "",
    @SerializedName("message")
    val message: String = ""
)
