package com.example.hashscanner.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.hashscanner.data.database.dao.AppDao
import com.example.hashscanner.data.database.dao.PermissionDao
import com.example.hashscanner.data.database.dao.ScanHistoryDao
import com.example.hashscanner.data.database.dao.SuspiciousDao
import com.example.hashscanner.data.model.db_entities.AppInfo
import com.example.hashscanner.data.model.db_entities.PermissionInfo
import com.example.hashscanner.data.model.db_entities.ScanHistory
import com.example.hashscanner.data.model.db_entities.SuspiciousApp

@Database(
    entities = [
        AppInfo::class,
        PermissionInfo::class,
        SuspiciousApp::class,
        ScanHistory::class
    ],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun appDao(): AppDao

    abstract fun permissionDao(): PermissionDao

    abstract fun suspiciousDao(): SuspiciousDao

    abstract fun scanHistoryDao(): ScanHistoryDao

}