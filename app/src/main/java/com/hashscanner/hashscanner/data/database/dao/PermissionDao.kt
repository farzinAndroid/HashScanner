package com.hashscanner.hashscanner.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hashscanner.hashscanner.data.model.db_entities.PermissionInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface PermissionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(permission: PermissionInfo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<PermissionInfo>)

    @Query("SELECT * FROM permissions WHERE packageName=:pkg")
    fun getPermissions(pkg:String): Flow<List<PermissionInfo>>

    @Query("DELETE FROM permissions")
    suspend fun deleteAll()
}