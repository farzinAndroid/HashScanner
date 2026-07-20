package com.example.hashscanner.data.model.db_entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "suspicious_apps")
data class SuspiciousApp(

    @PrimaryKey
    val packageName: String,

    val appName: String,

    val sha256: String,

    val riskScore: Int,

    val riskLevel: String,

    val reason: String,

    val reportDate: String,

    // آیا ارسال APK برای این برنامه پیشنهاد می‌شود؟
    val recommendUpload: Boolean = false,

    // دلیل پیشنهاد ارسال
    val recommendation: String = "",

    // آیا اطلاعات این برنامه به سرور ارسال شده است؟
    val sentToServer: Boolean = false,

    // آیا فایل APK این برنامه توسط کاربر ارسال شده است؟
    val apkUploaded: Boolean = false,

    // تاریخ و زمان ارسال APK
    val uploadDate: String = ""

)