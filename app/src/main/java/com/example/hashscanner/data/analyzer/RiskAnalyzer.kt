package com.example.hashscanner.data.analyzer

import android.Manifest
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo

data class RiskResult(

    val score: Int,

    val level: String,

    val reasons: String,

    val recommendUpload: Boolean,

    val recommendation: String

)

class RiskAnalyzer {

    fun analyze(

        pkg: PackageInfo,

        app: ApplicationInfo,

        installer: String

    ): RiskResult {

        var score = 0

        val reasons = mutableListOf<String>()

        if ((app.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0) {

            score += 15

            reasons.add("Debuggable Application")

        }

        if ((app.flags and ApplicationInfo.FLAG_SYSTEM) == 0) {

            score += 5

        }

        if (installer == "Unknown") {

            score += 20

            reasons.add("Unknown Installer")

        }

        pkg.requestedPermissions?.forEach {

            when (it) {

                Manifest.permission.SYSTEM_ALERT_WINDOW -> {

                    score += 20

                    reasons.add("Overlay Permission")

                }

                Manifest.permission.REQUEST_INSTALL_PACKAGES -> {

                    score += 20

                    reasons.add("Can Install APK")

                }

                Manifest.permission.BIND_ACCESSIBILITY_SERVICE -> {

                    score += 25

                    reasons.add("Accessibility Service")

                }

                Manifest.permission.RECORD_AUDIO -> {

                    score += 5

                    reasons.add("Microphone Access")

                }

                Manifest.permission.CAMERA -> {

                    score += 5

                    reasons.add("Camera Access")

                }

                Manifest.permission.READ_SMS -> {

                    score += 15

                    reasons.add("Read SMS")

                }

                Manifest.permission.SEND_SMS -> {

                    score += 15

                    reasons.add("Send SMS")

                }

                Manifest.permission.RECEIVE_BOOT_COMPLETED -> {

                    score += 10

                    reasons.add("Starts Automatically")

                }

            }

        }

        if (score > 100) {

            score = 100

        }

        val level = when {

            score >= 80 -> "CRITICAL"

            score >= 60 -> "HIGH"

            score >= 40 -> "MEDIUM"

            score >= 20 -> "LOW"

            else -> "SAFE"

        }

        val recommendUpload = score >= 60

        val recommendation = when {

            score >= 80 -> "This application has a very high risk score. It is strongly recommended to upload the APK for advanced malware analysis."

            score >= 60 -> "This application appears suspicious. Uploading the APK for further security analysis is recommended."

            score >= 40 -> "Some suspicious indicators were detected. Monitor this application carefully."

            else -> "No significant security risk detected."

        }

        return RiskResult(

            score = score,

            level = level,

            reasons = reasons.joinToString(", "),

            recommendUpload = recommendUpload,

            recommendation = recommendation

        )

    }

}