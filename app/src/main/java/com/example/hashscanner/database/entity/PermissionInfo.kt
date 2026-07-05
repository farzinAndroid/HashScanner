package com.example.hashscanner.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "permissions")
data class PermissionInfo(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val packageName: String,

    val permissionName: String,

    val dangerous: Boolean,

    val granted: Boolean
)