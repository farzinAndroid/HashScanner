package com.example.hashscanner.data.model.db_entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scan_history")
data class ScanHistory(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val scanDate: String,

    val scanTime: String,

    val totalApps: Int,

    val scannedApps: Int,

    val safeApps: Int,

    val lowRisk: Int,

    val mediumRisk: Int,

    val highRisk: Int,

    val criticalRisk: Int,

    val duration: Long
)