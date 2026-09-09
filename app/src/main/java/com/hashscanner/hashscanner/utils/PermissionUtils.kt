package com.hashscanner.hashscanner.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.hashscanner.hashscanner.ui.ui_utils.PermissionStep


object PermissionUtils {

    fun checkNotificationPermission(context: Context, onResult: (Boolean) -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permissionCheck = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            )
            onResult(permissionCheck != PackageManager.PERMISSION_GRANTED)
        } else {
            onResult(false)
        }
    }

    private fun isIgnoringBatteryOptimizations(context: Context): Boolean {
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        return powerManager.isIgnoringBatteryOptimizations(context.packageName)
    }

    fun isXiaomi(): Boolean = Build.MANUFACTURER.equals("Xiaomi", ignoreCase = true)

    @SuppressLint("BatteryLife")
    fun requestIgnoreBatteryOptimizations(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return
        if (isIgnoringBatteryOptimizations(context)) return

        val intent = if (isXiaomi()) {
            // For Xiaomi, taking them to App Info is often more successful
            // so they can find "Battery Saver" -> "No Restrictions"
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = android.net.Uri.fromParts("package", context.packageName, null)
            }
        } else {
            Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                data = "package:${context.packageName}".toUri()
            }
        }
        context.startActivity(intent)
    }

    fun performPermissionCheck(currentStep : PermissionStep, context: Context, onStepChanged: (PermissionStep) -> Unit) {
        if (!isIgnoringBatteryOptimizations(context)) {
            onStepChanged(PermissionStep.BATTERY)
        } else {
            checkNotificationPermission(context) { needsPermission ->
                onStepChanged(if (needsPermission) PermissionStep.NOTIFICATION else PermissionStep.NONE)
            }
        }
    }



}

