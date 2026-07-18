package com.example.hashscanner.data.analyzer

import android.Manifest
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import com.example.hashscanner.data.model.RiskLevels
import com.example.hashscanner.data.model.RiskResult
import com.example.hashscanner.utils.Constants
import javax.inject.Inject


class RiskAnalyzer @Inject constructor(){

    fun analyze(

        pkg: PackageInfo,

        app: ApplicationInfo,

        installer: String

    ): RiskResult {

        var score = 0

        val reasons = mutableListOf<String>()

        if ((app.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0) {

            score += 15

            reasons.add(Constants.RISK_REASON_DEBUGGABLE)

        }

        if ((app.flags and ApplicationInfo.FLAG_SYSTEM) == 0) {

            score += 5

        }

        if (installer == Constants.INSTALLER_UNKNOWN) {

            score += 20

            reasons.add(Constants.RISK_REASON_UNKNOWN_INSTALLER)

        }

        pkg.requestedPermissions?.forEach {

            when (it) {

                Manifest.permission.SYSTEM_ALERT_WINDOW -> {

                    score += 20

                    reasons.add(Constants.RISK_REASON_OVERLAY)

                }

                Manifest.permission.REQUEST_INSTALL_PACKAGES -> {

                    score += 20

                    reasons.add(Constants.RISK_REASON_INSTALL_PACKAGES)

                }

                Manifest.permission.BIND_ACCESSIBILITY_SERVICE -> {

                    score += 25

                    reasons.add(Constants.RISK_REASON_ACCESSIBILITY)

                }

                Manifest.permission.RECORD_AUDIO -> {

                    score += 5

                    reasons.add(Constants.RISK_REASON_MICROPHONE)

                }

                Manifest.permission.CAMERA -> {

                    score += 5

                    reasons.add(Constants.RISK_REASON_CAMERA)

                }

                Manifest.permission.READ_SMS -> {

                    score += 15

                    reasons.add(Constants.RISK_REASON_READ_SMS)

                }

                Manifest.permission.SEND_SMS -> {

                    score += 15

                    reasons.add(Constants.RISK_REASON_SEND_SMS)

                }

                Manifest.permission.RECEIVE_BOOT_COMPLETED -> {

                    score += 10

                    reasons.add(Constants.RISK_REASON_AUTO_START)

                }

            }

        }

        if (score > 100) {

            score = 100

        }

        val level = when {

            score >= 80 -> RiskLevels.CRITICAL.toString()

            score >= 60 -> RiskLevels.HIGH.toString()

            score >= 40 -> RiskLevels.MEDIUM.toString()

            score >= 20 -> RiskLevels.LOW.toString()

            else -> RiskLevels.SAFE.toString()

        }

        val recommendUpload = score >= 60

        val recommendation = when {

            score >= 80 -> Constants.RISK_REC_CRITICAL

            score >= 60 -> Constants.RISK_REC_HIGH

            score >= 40 -> Constants.RISK_REC_MEDIUM

            else -> Constants.RISK_REC_SAFE

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