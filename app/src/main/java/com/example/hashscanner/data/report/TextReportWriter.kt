package com.example.hashscanner.data.report

import com.example.hashscanner.data.model.db_entities.AppInfo
import com.example.hashscanner.utils.Constants
import java.io.File

class TextReportWriter {

    fun write(

        apps: List<AppInfo>,

        outputFile: File

    ) {

        val report = StringBuilder()

        report.appendLine("========================================")
        report.appendLine("        ${Constants.EXPORT_LABEL_REPORT_TITLE.uppercase()}")
        report.appendLine("========================================")
        report.appendLine()

        report.appendLine("${Constants.EXPORT_LABEL_TOTAL_APPS} : ${apps.size}")
        report.appendLine("")

        apps.forEach { app ->

            report.appendLine("----------------------------------------")

            report.appendLine(Constants.EXPORT_LABEL_APPLICATION)
            report.appendLine("Name : ${app.appName}")
            report.appendLine("${Constants.EXPORT_LABEL_PACKAGE} : ${app.packageName}")

            report.appendLine()

            report.appendLine(Constants.EXPORT_LABEL_VERSION)
            report.appendLine("Version Name : ${app.versionName}")
            report.appendLine("Version Code : ${app.versionCode}")

            report.appendLine()

            report.appendLine(Constants.EXPORT_LABEL_APK_NAME)
            report.appendLine("${Constants.EXPORT_LABEL_APK_NAME} : ${app.apkName}")
            report.appendLine("${Constants.EXPORT_LABEL_APK_PATH} : ${app.apkPath}")
            report.appendLine("${Constants.EXPORT_LABEL_APK_SIZE} : ${app.apkSize}")

            report.appendLine()

            report.appendLine(Constants.EXPORT_LABEL_SHA256)
            report.appendLine("${Constants.EXPORT_LABEL_MD5} : ${app.md5}")
            report.appendLine("${Constants.EXPORT_LABEL_SHA1} : ${app.sha1}")
            report.appendLine("${Constants.EXPORT_LABEL_SHA256} : ${app.sha256}")

            report.appendLine()

            report.appendLine(Constants.EXPORT_LABEL_CERT_SHA256)
            report.appendLine("${Constants.EXPORT_LABEL_SHA1} : ${app.certificateSha1}")
            report.appendLine("${Constants.EXPORT_LABEL_SHA256} : ${app.certificateSha256}")
            report.appendLine("${Constants.EXPORT_LABEL_ISSUER} : ${app.certificateIssuer}")
            report.appendLine("${Constants.EXPORT_LABEL_SUBJECT} : ${app.certificateSubject}")
            report.appendLine("${Constants.EXPORT_LABEL_SERIAL} : ${app.certificateSerial}")
            report.appendLine("${Constants.EXPORT_LABEL_ALGORITHM} : ${app.certificateAlgorithm}")
            report.appendLine("${Constants.EXPORT_LABEL_VALID_FROM} : ${app.certificateValidFrom}")
            report.appendLine("${Constants.EXPORT_LABEL_VALID_TO} : ${app.certificateValidTo}")

            report.appendLine()

            report.appendLine(Constants.EXPORT_LABEL_TARGET_SDK)
            report.appendLine("${Constants.EXPORT_LABEL_INSTALLER} : ${app.installer}")
            report.appendLine("${Constants.EXPORT_LABEL_TARGET_SDK} : ${app.targetSdk}")
            report.appendLine("${Constants.EXPORT_LABEL_MIN_SDK} : ${app.minSdk}")
            report.appendLine("${Constants.EXPORT_LABEL_SYSTEM_APP} : ${app.isSystem}")
            report.appendLine("${Constants.EXPORT_LABEL_DEBUGGABLE} : ${app.isDebuggable}")
            report.appendLine("${Constants.EXPORT_LABEL_ENABLED} : ${app.isEnabled}")

            report.appendLine()

            report.appendLine(Constants.EXPORT_LABEL_RISK_LEVEL)
            report.appendLine("${Constants.EXPORT_LABEL_RISK_SCORE} : ${app.riskScore}")
            report.appendLine("${Constants.EXPORT_LABEL_RISK_LEVEL} : ${app.riskLevel}")
            report.appendLine("${Constants.EXPORT_LABEL_SUSPICIOUS} : ${app.suspicious}")
            report.appendLine("${Constants.EXPORT_LABEL_REASONS} : ${app.riskReasons}")

            report.appendLine()

            report.appendLine(Constants.EXPORT_LABEL_VIRUSTOTAL)
            report.appendLine("${Constants.EXPORT_LABEL_CHECKED} : ${app.vtChecked}")
            report.appendLine("${Constants.EXPORT_LABEL_RESULT} : ${app.vtResult}")

            report.appendLine()

            report.appendLine(Constants.EXPORT_LABEL_SCAN)
            report.appendLine("${Constants.EXPORT_LABEL_DATE} : ${app.scanDate}")
            report.appendLine("${Constants.EXPORT_LABEL_TIME} : ${app.scanTime}")

            report.appendLine()

        }

        outputFile.parentFile?.mkdirs()

        outputFile.writeText(report.toString())

    }

}