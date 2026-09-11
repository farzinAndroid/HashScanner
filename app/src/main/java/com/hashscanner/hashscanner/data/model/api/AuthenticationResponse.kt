package com.hashscanner.hashscanner.data.model.api

import com.google.gson.annotations.SerializedName

data class AuthenticationResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Boolean
)
