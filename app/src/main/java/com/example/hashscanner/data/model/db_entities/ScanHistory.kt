package com.example.hashscanner.data.model.db_entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scan_history")
data class ScanHistory(

    @PrimaryKey
    val id: String,
    val scanDate: String,
    val scanTime: String,
    val totalApps: Int,
    val scannedApps: Int,

    val systemApps: Int,
    val userApps: Int,

    val safeApps: Int,
    val lowRisk: Int,
    val mediumRisk: Int,
    val highRisk: Int,
    val criticalRisk: Int,

    val safeUserApps: Int,
    val lowRiskUserApps: Int,
    val mediumRiskUserApps: Int,
    val highRiskUserApps: Int,
    val criticalRiskUserApps: Int,
    
    val duration: Long
)