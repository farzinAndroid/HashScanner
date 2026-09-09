package com.hashscanner.hashscanner.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hashscanner.hashscanner.data.database.dao.AppDao
import com.hashscanner.hashscanner.data.database.dao.PermissionDao
import com.hashscanner.hashscanner.data.database.dao.ScanHistoryDao
import com.hashscanner.hashscanner.data.database.dao.SuspiciousDao
import com.hashscanner.hashscanner.data.model.db_entities.AppInfo
import com.hashscanner.hashscanner.data.model.db_entities.PermissionInfo
import com.hashscanner.hashscanner.data.model.db_entities.ScanHistory
import com.hashscanner.hashscanner.data.model.db_entities.SuspiciousApp

@Database(
    entities = [
        AppInfo::class,
        PermissionInfo::class,
        SuspiciousApp::class,
        ScanHistory::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun appDao(): AppDao

    abstract fun permissionDao(): PermissionDao

    abstract fun suspiciousDao(): SuspiciousDao

    abstract fun scanHistoryDao(): ScanHistoryDao

}
