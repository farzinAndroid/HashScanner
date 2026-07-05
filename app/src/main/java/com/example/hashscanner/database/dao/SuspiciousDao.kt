package com.example.hashscanner.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.hashscanner.database.entity.SuspiciousApp

@Dao
interface SuspiciousDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(app: SuspiciousApp)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(apps: List<SuspiciousApp>)

    @Update
    suspend fun update(app: SuspiciousApp)

    @Query("SELECT * FROM suspicious_apps")
    suspend fun getAll(): List<SuspiciousApp>

    @Query("SELECT * FROM suspicious_apps WHERE sentToServer=0")
    suspend fun getNotSent(): List<SuspiciousApp>

    @Query("UPDATE suspicious_apps SET sentToServer=1 WHERE packageName=:pkg")
    suspend fun markUploaded(pkg: String)

    @Query("SELECT COUNT(*) FROM suspicious_apps")
    suspend fun count(): Int

    @Query("DELETE FROM suspicious_apps")
    suspend fun deleteAll()

}