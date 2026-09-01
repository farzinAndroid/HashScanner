package com.example.hashscanner.utils

import android.content.Context
import android.content.pm.PackageManager

object PackageUtils {
    fun isPackageInstalled(context: Context, packageName: String): Boolean {
        return try {
            context.packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }
}
