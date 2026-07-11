package com.example.hashscanner.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.hashscanner.data.database.entity.SuspiciousApp

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
    suspend fun getAll(): List<SuspiciousApp>

    @Query("SELECT * FROM suspicious_apps WHERE packageName=:pkg LIMIT 1")
    suspend fun getByPackage(pkg: String): SuspiciousApp?

    @Query("SELECT * FROM suspicious_apps WHERE sentToServer=0 ORDER BY riskScore DESC")
    suspend fun getNotSent(): List<SuspiciousApp>

    @Query("SELECT * FROM suspicious_apps WHERE recommendUpload=1 ORDER BY riskScore DESC")
    suspend fun getRecommended(): List<SuspiciousApp>

    @Query("SELECT COUNT(*) FROM suspicious_apps")
    suspend fun count(): Int

    @Query("SELECT COUNT(*) FROM suspicious_apps WHERE recommendUpload=1")
    suspend fun countRecommended(): Int

    @Query("SELECT COUNT(*) FROM suspicious_apps WHERE sentToServer=0")
    suspend fun countNotSent(): Int

    @Query("""
        UPDATE suspicious_apps
        SET sentToServer = 1,
            apkUploaded = 1,
            uploadDate = :date
        WHERE packageName = :pkg
    """)
    suspend fun markUploaded(
        pkg: String,
        date: String
    )

}