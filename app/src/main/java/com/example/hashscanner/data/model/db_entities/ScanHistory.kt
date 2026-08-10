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
    
    // Total Counts
    val systemApps: Int,
    val userApps: Int,

    // Risk Levels (Total)
    val safeApps: Int,
    val lowRisk: Int,
    val mediumRisk: Int,
    val highRisk: Int,
    val criticalRisk: Int,
    
    // Risk Levels (User Only)
    val safeUserApps: Int,
    val lowRiskUserApps: Int,
    val mediumRiskUserApps: Int,
    val highRiskUserApps: Int,
    val criticalRiskUserApps: Int,
    
    val duration: Long
)