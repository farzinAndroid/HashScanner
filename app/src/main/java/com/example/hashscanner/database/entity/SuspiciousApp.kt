package com.example.hashscanner.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "suspicious_apps")
data class SuspiciousApp(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val appName: String,

    val packageName: String,

    val sha256: String,

    val riskScore: Int,

    val riskLevel: String,

    val reason: String,

    val sentToServer: Boolean,

    val reportDate: String
)