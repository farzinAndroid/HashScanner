package com.example.hashscanner.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.hashscanner.data.database.entity.PermissionInfo

@Dao
interface PermissionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(permission: PermissionInfo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<PermissionInfo>)

    @Query("SELECT * FROM permissions WHERE packageName=:pkg")
    suspend fun getPermissions(pkg:String): List<PermissionInfo>

    @Query("DELETE FROM permissions")
    suspend fun deleteAll()
}