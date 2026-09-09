package com.hashscanner.hashscanner.data.model.other

data class RiskResult(

    val score: Int,

    val level: String,

    val reasons: String,

    val recommendUpload: Boolean,

    val recommendation: String

)