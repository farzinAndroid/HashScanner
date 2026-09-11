package com.hashscanner.hashscanner.data.model.api

import com.google.gson.annotations.SerializedName

data class UserAuthentication(
    @SerializedName("deviceId")
    val deviceId: String,
    @SerializedName("activationCode")
    val activationCode: String
)
