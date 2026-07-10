package com.example.hashscanner.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.hashscanner.data.database.dao.AppDao
import com.example.hashscanner.data.database.dao.PermissionDao
import com.example.hashscanner.data.database.dao.ScanHistoryDao
import com.example.hashscanner.data.database.dao.SuspiciousDao
import com.example.hashscanner.data.database.entity.AppInfo
import com.example.hashscanner.data.database.entity.PermissionInfo
import com.example.hashscanner.data.database.entity.ScanHistory
import com.example.hashscanner.data.database.entity.SuspiciousApp

@Database(
    entities = [
        AppInfo::class,
        PermissionInfo::class,
        SuspiciousApp::class,
        ScanHistory::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun appDao(): AppDao

    abstract fun permissionDao(): PermissionDao

    abstract fun suspiciousDao(): SuspiciousDao

    abstract fun scanHistoryDao(): ScanHistoryDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(
            context: Context
        ): AppDatabase {

            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_hash_scanner.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance

                instance
            }

        }

    }

}