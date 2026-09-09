package com.hashscanner.hashscanner.data.model.db_entities

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "apps",
    primaryKeys = ["packageName", "scanId"],
    indices = [
        Index(value = ["scanId"]),
        Index(value = ["sha256"])
    ]
)
data class AppInfo(
    val appName: String,
    val iconData: ByteArray?,
    val packageName: String,
    val scanId: String,
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
    val certificateValidFrom: String,
    val certificateValidTo: String,
    val installer: String,
    val firstInstallTime: Long,
    val lastUpdateTime: Long,
    val targetSdk: Int,
    val minSdk: Int,
    val isSystem: Boolean,
    val isDebuggable: Boolean,
    val isEnabled: Boolean,
    val riskScore: Int,
    val riskLevel: String,
    val suspicious: Boolean,
    val riskReasons: String,
    val recommendUpload: Boolean,
    val recommendation: String,
    val vtChecked: Boolean,
    val vtResult: String,
    val scanDate: String,
    val scanTime: String,
    val apkUploaded: Boolean = false,
    val uploadDate: String = "",
    val isDeleted: Boolean = false,
    val isServerVerified: Boolean = false,
    val serverResult: String = "",
    val serverAction: String = ""
)
