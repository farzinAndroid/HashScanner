package com.example.hashscanner.scanner

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build

data class ManifestInfo(

    val activityCount: Int,

    val serviceCount: Int,

    val receiverCount: Int,

    val providerCount: Int,

    val exportedActivities: Int,

    val exportedServices: Int,

    val exportedReceivers: Int,

    val exportedProviders: Int

)

class ManifestScanner(

    private val context: Context

) {

    private val pm = context.packageManager

    fun scan(

        packageName: String

    ): ManifestInfo {

        return try {

            val info =

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

                    pm.getPackageInfo(

                        packageName,

                        PackageManager.PackageInfoFlags.of(

                            (
                                PackageManager.GET_ACTIVITIES or
                                PackageManager.GET_SERVICES or
                                PackageManager.GET_RECEIVERS or
                                PackageManager.GET_PROVIDERS
                            ).toLong()

                        )

                    )

                } else {

                    @Suppress("DEPRECATION")

                    pm.getPackageInfo(

                        packageName,

                        PackageManager.GET_ACTIVITIES or
                                PackageManager.GET_SERVICES or
                                PackageManager.GET_RECEIVERS or
                                PackageManager.GET_PROVIDERS

                    )

                }

            ManifestInfo(

                activityCount =
                info.activities?.size ?: 0,

                serviceCount =
                info.services?.size ?: 0,

                receiverCount =
                info.receivers?.size ?: 0,

                providerCount =
                info.providers?.size ?: 0,

                exportedActivities =
                info.activities?.count { it.exported } ?: 0,

                exportedServices =
                info.services?.count { it.exported } ?: 0,

                exportedReceivers =
                info.receivers?.count { it.exported } ?: 0,

                exportedProviders =
                info.providers?.count { it.exported } ?: 0

            )

        } catch (e: Exception) {

            ManifestInfo(

                0,

                0,

                0,

                0,

                0,

                0,

                0,

                0

            )

        }

    }

}