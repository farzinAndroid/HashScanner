package com.hashscanner.hashscanner.data.model.api

import com.google.gson.annotations.SerializedName

data class ScanResultModel(
    @SerializedName("scanId")
    val scanId: String,
    @SerializedName("deviceId")
    val deviceId: String
)
