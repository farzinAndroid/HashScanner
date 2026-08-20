package com.example.hashscanner.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.hashscanner.data.model.db_entities.ScanHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface ScanHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(history: ScanHistory)

    @Query("SELECT * FROM scan_history ORDER BY id DESC")
    fun getAll(): Flow<List<ScanHistory>>

    @Query("SELECT * FROM scan_history ORDER BY id DESC LIMIT 1")
    fun getLastScan(): Flow<ScanHistory?>

    @Query("DELETE FROM scan_history")
    suspend fun deleteAll()


    @Query("DELETE FROM scan_history WHERE id=:id")
    suspend fun deleteScanHistory(id: String)

    @Query("SELECT * FROM scan_history WHERE analysisStatus = :pendingStatus")
    fun getPendingScans(pendingStatus: String): Flow<List<ScanHistory>>

    @Query("UPDATE scan_history SET analysisStatus = :status WHERE id = :id")
    suspend fun updateAnalysisStatus(id: String, status: String)

    @Query("UPDATE scan_history SET lastNotifiedStage = :stage WHERE id = :id")
    suspend fun updateLastNotifiedStage(id: String, stage: String)

    @Query("SELECT * FROM scan_history WHERE id = :id")
    suspend fun getScanById(id: String): ScanHistory?
}