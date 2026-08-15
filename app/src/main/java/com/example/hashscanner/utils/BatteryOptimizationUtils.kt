package com.example.hashscanner.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import androidx.core.net.toUri

/**
 * Whether the app is currently exempt from Doze / App Standby battery
 * optimizations. When false, the OS is free to aggressively defer this
 * app's WorkManager/JobScheduler jobs while the device is idle — this is
 * the main reason a periodic worker can appear to "not run" for well
 * beyond its configured interval while the app is closed.
 */
fun isIgnoringBatteryOptimizations(context: Context): Boolean {
    val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
    return powerManager.isIgnoringBatteryOptimizations(context.packageName)
}

/**
 * Launches the system dialog letting the user exempt this app from Doze.
 * This is a "special" permission — it can't be requested via the normal
 * runtime permission flow, only via this direct-to-Settings intent, and
 * the user can always decline it from the dialog that appears.
 */
@SuppressLint("BatteryLife")
fun requestIgnoreBatteryOptimizations(context: Context) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return
    if (isIgnoringBatteryOptimizations(context)) return

    val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
        data = "package:${context.packageName}".toUri()
    }
    context.startActivity(intent)
}
