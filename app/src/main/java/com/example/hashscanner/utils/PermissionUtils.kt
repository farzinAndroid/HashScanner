package com.example.hashscanner.utils

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
import com.example.hashscanner.ui.ui_utils.PermissionStep


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

    fun isIgnoringBatteryOptimizations(context: Context): Boolean {
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        return powerManager.isIgnoringBatteryOptimizations(context.packageName)
    }

    @SuppressLint("BatteryLife")
    fun requestIgnoreBatteryOptimizations(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return
        if (isIgnoringBatteryOptimizations(context)) return

        val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
            data = "package:${context.packageName}".toUri()
        }
        context.startActivity(intent)
    }

    fun performPermissionCheck(currentStep : PermissionStep, context: Context, onStepChanged: (PermissionStep) -> Unit) {
        if (!PermissionUtils.isIgnoringBatteryOptimizations(context)) {
            onStepChanged(PermissionStep.BATTERY)
        } else {
            checkNotificationPermission(context) { needsPermission ->
                onStepChanged(if (needsPermission) PermissionStep.NOTIFICATION else PermissionStep.NONE)
            }
        }
    }



}

