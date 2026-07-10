package com.example.hashscanner.data.report

import com.example.hashscanner.data.database.entity.AppInfo
import java.io.File

class TextReportWriter {

    fun write(

        apps: List<AppInfo>,

        outputFile: File

    ) {

        val report = StringBuilder()

        report.appendLine("========================================")
        report.appendLine("        APP HASH SCANNER REPORT")
        report.appendLine("========================================")
        report.appendLine()

        report.appendLine("Total Apps : ${apps.size}")
        report.appendLine("")

        apps.forEach { app ->

            report.appendLine("----------------------------------------")

            report.appendLine("Application")
            report.appendLine("Name : ${app.appName}")
            report.appendLine("Package : ${app.packageName}")

            report.appendLine()

            report.appendLine("Version")
            report.appendLine("Version Name : ${app.versionName}")
            report.appendLine("Version Code : ${app.versionCode}")

            report.appendLine()

            report.appendLine("APK")
            report.appendLine("APK Name : ${app.apkName}")
            report.appendLine("APK Path : ${app.apkPath}")
            report.appendLine("APK Size : ${app.apkSize}")

            report.appendLine()

            report.appendLine("Hashes")
            report.appendLine("MD5 : ${app.md5}")
            report.appendLine("SHA1 : ${app.sha1}")
            report.appendLine("SHA256 : ${app.sha256}")

            report.appendLine()

            report.appendLine("Certificate")
            report.appendLine("SHA1 : ${app.certificateSha1}")
            report.appendLine("SHA256 : ${app.certificateSha256}")
            report.appendLine("Issuer : ${app.certificateIssuer}")
            report.appendLine("Subject : ${app.certificateSubject}")
            report.appendLine("Serial : ${app.certificateSerial}")
            report.appendLine("Algorithm : ${app.certificateAlgorithm}")
            report.appendLine("Valid From : ${app.certificateValidFrom}")
            report.appendLine("Valid To : ${app.certificateValidTo}")

            report.appendLine()

            report.appendLine("Android")
            report.appendLine("Installer : ${app.installer}")
            report.appendLine("Target SDK : ${app.targetSdk}")
            report.appendLine("Min SDK : ${app.minSdk}")
            report.appendLine("System App : ${app.isSystem}")
            report.appendLine("Debuggable : ${app.isDebuggable}")
            report.appendLine("Enabled : ${app.isEnabled}")

            report.appendLine()

            report.appendLine("Risk Analysis")
            report.appendLine("Risk Score : ${app.riskScore}")
            report.appendLine("Risk Level : ${app.riskLevel}")
            report.appendLine("Suspicious : ${app.suspicious}")
            report.appendLine("Reasons : ${app.riskReasons}")

            report.appendLine()

            report.appendLine("VirusTotal")
            report.appendLine("Checked : ${app.vtChecked}")
            report.appendLine("Result : ${app.vtResult}")

            report.appendLine()

            report.appendLine("Scan")
            report.appendLine("Date : ${app.scanDate}")
            report.appendLine("Time : ${app.scanTime}")

            report.appendLine()

        }

        outputFile.parentFile?.mkdirs()

        outputFile.writeText(report.toString())

    }

}