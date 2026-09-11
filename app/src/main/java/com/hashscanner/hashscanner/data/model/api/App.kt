package com.hashscanner.hashscanner.data.model.api

import com.google.gson.annotations.SerializedName

data class App(
    @SerializedName("appName")
    val appName: String,
    @SerializedName("packageName")
    val packageName: String,
    @SerializedName("reason")
    val reason: String,
    @SerializedName("recommendUpload")
    val recommendUpload: Boolean,
    @SerializedName("recommendation")
    val recommendation: String,
    @SerializedName("reportDate")
    val reportDate: String,
    @SerializedName("riskLevel")
    val riskLevel: String,
    @SerializedName("riskScore")
    val riskScore: Int,
    @SerializedName("sha256")
    val sha256: String
)
