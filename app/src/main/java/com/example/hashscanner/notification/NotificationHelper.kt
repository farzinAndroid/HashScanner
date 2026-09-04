package com.example.hashscanner.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.app.PendingIntent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.hashscanner.MainActivity
import com.example.hashscanner.R
import com.example.hashscanner.utils.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class NotificationHelper @Inject constructor(
    @ApplicationContext private val context: Context,
    private val notificationManager: NotificationManager,
    private val notificationBuilderProvider: Provider<NotificationCompat.Builder>
) {

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.notification_channel_name)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(Constants.NOTIFICATION_CHANNEL_ID, name, importance)
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showScanResultNotification(scanId: String, message: String) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("EXTRA_SCAN_ID", scanId)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            scanId.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = notificationBuilderProvider.get()
            .setContentTitle(context.getString(R.string.notification_title_cloud_analysis))
            .setContentText(message)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(scanId.hashCode(), notification)
    }

    fun showTestNotification(count: Int) {
        val notification = notificationBuilderProvider.get()
            .setContentTitle(context.getString(R.string.notification_test_title))
            .setContentText(context.getString(R.string.notification_test_message, count))
            .build()

        // Using 'count' as the ID so notifications stack instead of overwriting
        notificationManager.notify(count, notification)
    }
}
