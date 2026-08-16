package com.example.hashscanner.data.model.api

import com.google.gson.annotations.SerializedName

data class ScanResultResponse(
    val ready: Boolean,
    val stage: String? = null,
    val message: String? = null,
    val totalReports: Int? = null,
    val checkedReports: Int? = null,
    val pendingReports: Int? = null,
    val initialReport: InitialReport? = null,
    val uploadedApks: UploadedApks? = null,
    val finalReport: FinalReport? = null
)

data class InitialReport(
    val complete: Boolean,
    val summary: ReportSummary,
    val apps: List<ApiAppResult>
)

data class ReportSummary(
    val total: Int,
    val virus: Int,
    val suspicious: Int,
    val safe: Int,
    val virusApps: List<String>,
    val suspiciousApps: List<String>,
    val safeApps: List<String>
)

data class UploadedApks(
    val summary: UploadSummary,
    val apps: List<ApiAppResult>
)

data class UploadSummary(
    val total: Int,
    val checked: Int,
    val pending: Int,
    val virus: Int,
    val safe: Int,
    val suspicious: Int,
    val virusApps: List<String>,
    val safeApps: List<String>,
    val suspiciousApps: List<String>,
    val pendingApps: List<String>,
    val complete: Boolean
)

data class FinalReport(
    val complete: Boolean,
    val summary: FinalSummary,
    val message: String? = null
)

data class FinalSummary(
    val total: Int,
    val virus: Int,
    val safe: Int,
    val pending: Int,
    val virusApps: List<String>,
    val safeApps: List<String>,
    val pendingApps: List<String>
)

data class ApiAppResult(
    val appName: String,
    val packageName: String,
    val sha256: String,
    val riskScore: Int,
    val riskLevel: String,
    val reason: String,
    val result: String,
    val action: String,
    val message: String
)
