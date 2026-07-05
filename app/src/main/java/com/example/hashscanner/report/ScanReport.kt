package com.example.hashscanner.report

data class ScanReport(

    val totalApps: Int,

    val systemApps: Int,

    val userApps: Int,

    val suspiciousApps: Int,

    val highRiskApps: Int,

    val mediumRiskApps: Int,

    val lowRiskApps: Int,

    val safeApps: Int,

    val scanDate: String,

    val scanTime: String

)