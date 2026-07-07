package com.example.hashscanner.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "apps",
    indices = [
        Index(value = ["packageName"], unique = true),
        Index(value = ["sha256"])
    ]
)
data class AppInfo(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val appName: String,

    val packageName: String,

    val versionName: String,

    val versionCode: Long,

    val apkName: String,

    val apkPath: String,

    val apkSize: Long,

    val md5: String,

    val sha1: String,

    val sha256: String,

    val certificateSha1: String,

    val certificateSha256: String,

    val certificateIssuer: String,

    val certificateSubject: String,

    val certificateSerial: String,

    val certificateAlgorithm: String,

    val certificateValidFrom: Long,

    val certificateValidTo: Long,

    val installer: String,

    val firstInstallTime: Long,

    val lastUpdateTime: Long,

    val targetSdk: Int,

    val minSdk: Int,

    val isSystem: Boolean,

    val isDebuggable: Boolean,

    val isEnabled: Boolean,

    // ---------- Risk ----------

    val riskScore: Int,

    val riskLevel: String,

    val suspicious: Boolean,

    val riskReasons: String,

    // ---------- Recommendation ----------

    val recommendUpload: Boolean,

    val recommendation: String,

    // ---------- VirusTotal ----------

    val vtChecked: Boolean,

    val vtResult: String,

    // ---------- Scan ----------

    val scanDate: String,

    val scanTime: String

)