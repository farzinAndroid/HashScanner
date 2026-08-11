package com.example.hashscanner.data.model.api

data class ScanResultResponse(
    val ready: Boolean,
    val summary: ScanSummary? = null,
    val apps: List<ApiAppResult>? = null
)

data class ScanSummary(
    val result: String,
    val message: String
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