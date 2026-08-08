package com.example.hashscanner.data.model.api

data class App(
    val appName: String,
    val packageName: String,
    val reason: String,
    val recommendUpload: Boolean,
    val recommendation: String,
    val reportDate: String,
    val riskLevel: String,
    val riskScore: Int,
    val sha256: String
)