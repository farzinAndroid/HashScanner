package com.example.hashscanner.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.hashscanner.data.database.entity.ScanHistory

@Dao
interface ScanHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(history: ScanHistory)

    @Query("SELECT * FROM scan_history ORDER BY id DESC")
    suspend fun getAll(): List<ScanHistory>

    @Query("SELECT * FROM scan_history ORDER BY id DESC LIMIT 1")
    suspend fun getLastScan(): ScanHistory?

    @Query("DELETE FROM scan_history")
    suspend fun deleteAll()
}