package com.example.hashscanner.data.model

data class RiskResult(

    val score: Int,

    val level: String,

    val reasons: String,

    val recommendUpload: Boolean,

    val recommendation: String

)