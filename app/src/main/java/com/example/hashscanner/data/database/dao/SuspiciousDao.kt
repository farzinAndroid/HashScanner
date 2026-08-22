package com.example.hashscanner.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.hashscanner.data.model.db_entities.SuspiciousApp
import kotlinx.coroutines.flow.Flow

@Dao
interface SuspiciousDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(app: SuspiciousApp)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(apps: List<SuspiciousApp>)

    @Update
    suspend fun update(app: SuspiciousApp)

    @Delete
    suspend fun delete(app: SuspiciousApp)

    @Query("DELETE FROM suspicious_apps")
    suspend fun deleteAll()

    @Query("SELECT * FROM suspicious_apps ORDER BY riskScore DESC")
    fun getAll(): Flow<List<SuspiciousApp>>

    @Query("SELECT * FROM suspicious_apps WHERE packageName=:pkg LIMIT 1")
    fun getByPackage(pkg: String): Flow<SuspiciousApp?>

    @Query("SELECT * FROM suspicious_apps WHERE sentToServer=0 ORDER BY riskScore DESC")
    fun getNotSent(): Flow<List<SuspiciousApp>>

    @Query("SELECT * FROM suspicious_apps WHERE recommendUpload=1 ORDER BY riskScore DESC")
    fun getRecommended(): Flow<List<SuspiciousApp>>

    @Query("SELECT COUNT(*) FROM suspicious_apps")
    fun count(): Flow<Int>

    @Query("SELECT COUNT(*) FROM suspicious_apps WHERE recommendUpload=1")
    fun countRecommended(): Flow<Int>

    @Query("SELECT COUNT(*) FROM suspicious_apps WHERE sentToServer=0")
    fun countNotSent(): Flow<Int>

    @Query("SELECT COUNT(*) FROM suspicious_apps WHERE sentToServer=1")
    fun countSent(): Flow<Int>

    @Query("""
        UPDATE suspicious_apps
        SET sentToServer = 1,
            uploadDate = :date
        WHERE packageName = :pkg
    """)
    suspend fun markReportSent(
        pkg: String,
        date: String
    )

    @Query("""
        UPDATE suspicious_apps
        SET apkUploaded = 1,
            uploadDate = :date
        WHERE sha256 = :hash
    """)
    suspend fun markApkUploadedByHash(
        hash: String,
        date: String
    )

}