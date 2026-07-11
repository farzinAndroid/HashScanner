package com.example.hashscanner.data.scanner

import android.Manifest
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import com.example.hashscanner.data.analyzer.RiskAnalyzer
import com.example.hashscanner.data.crypto.HashUtil
import com.example.hashscanner.data.database.AppDatabase
import com.example.hashscanner.data.database.dao.AppDao
import com.example.hashscanner.data.database.dao.PermissionDao
import com.example.hashscanner.data.database.dao.SuspiciousDao
import com.example.hashscanner.data.database.entity.AppInfo
import com.example.hashscanner.data.database.entity.PermissionInfo
import com.example.hashscanner.data.database.entity.SuspiciousApp
import java.io.File
import java.time.LocalDate
import java.time.LocalTime

class PackageScanner(
    private val context: Context,
    private val db: AppDatabase
) {

    private val pm = context.packageManager

    suspend fun startScan() {

        val appDao = db.appDao()
        val permissionDao = db.permissionDao()
        val suspiciousDao = db.suspiciousDao()

        val packages =

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

                pm.getInstalledPackages(

                    PackageManager.PackageInfoFlags.of(
                        PackageManager.GET_PERMISSIONS.toLong()
                    )

                )

            } else {

                @Suppress("DEPRECATION")

                pm.getInstalledPackages(
                    PackageManager.GET_PERMISSIONS
                )

            }

        for (pkg in packages) {

            //this is for later
            /*
            currentAppCount++
            val appName = pkg.applicationInfo?.let { pm.getApplicationLabel(it).toString() } ?: pkg.packageName


            onProgress(currentAppCount, totalApps, appName)*/

            /*val iconBitmap = try {
                val drawable = pkg?.let { pm.getApplicationIcon(it.applicationInfo!!) } ?: pm.defaultActivityIcon
                drawable.toBitmap() // Converts Android Drawable to a standard Bitmap
            } catch (e: Exception) {
                null // If an app has a corrupted icon, we safely ignore it
            }*/

            try {

                scanPackage(
                    pkg,
                    appDao,
                    permissionDao,
                    suspiciousDao
                )

            } catch (e: Exception) {

                e.printStackTrace()

            }

        }

    }

    private suspend fun scanPackage(

        pkg: PackageInfo,

        appDao: AppDao,

        permissionDao: PermissionDao,

        suspiciousDao: SuspiciousDao

    ) {

        val app = pkg.applicationInfo ?: return

        val appName =
            pm.getApplicationLabel(app).toString()

        val packageName =
            pkg.packageName

        val versionName =
            pkg.versionName ?: ""

        val versionCode =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                pkg.longVersionCode
            else
                @Suppress("DEPRECATION")
                pkg.versionCode.toLong()

        val apk =
            File(app.sourceDir)

        if (!apk.exists())
            return

        val md5 =
            HashUtil.md5(apk)

        val sha1 =
            HashUtil.sha1(apk)

        val sha256 =
            HashUtil.sha256(apk)

        val installer = try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

                pm.getInstallSourceInfo(packageName)
                    .installingPackageName ?: "Unknown"

            } else {

                @Suppress("DEPRECATION")
                pm.getInstallerPackageName(packageName)
                    ?: "Unknown"

            }

        } catch (e: Exception) {

            "Unknown"

        }

        val analyzer = RiskAnalyzer()

        val result = analyzer.analyze(

            pkg,

            app,

            installer

        )

        val risk = result.score

        val level = result.level
        val signatureScanner = SignatureScanner(context)
        val certificateScanner = CertificateScanner(context)

        val signature = signatureScanner.getSignature(packageName)
        val certificate = certificateScanner.getCertificate(packageName)


        val appInfo = AppInfo(

            appName = appName,

            packageName = packageName,

            versionName = versionName,

            versionCode = versionCode,

            apkName = apk.name,

            apkPath = apk.absolutePath,

            apkSize = apk.length(),

            md5 = md5,

            sha1 = sha1,

            sha256 = sha256,

            certificateSha1 =
                signature.sha1,

            certificateSha256 =
                signature.sha256,

            certificateIssuer =
                certificate.issuer,

            certificateSubject =
                certificate.subject,

            certificateSerial =
                certificate.serial,

            certificateAlgorithm =
                certificate.algorithm,

            certificateValidFrom =
                certificate.validFrom,

            certificateValidTo =
                certificate.validTo,

            installer = installer,

            firstInstallTime = pkg.firstInstallTime,

            lastUpdateTime = pkg.lastUpdateTime,

            targetSdk = app.targetSdkVersion,

            minSdk =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    app.minSdkVersion
                else
                    0,

            isSystem =
                (app.flags and ApplicationInfo.FLAG_SYSTEM) != 0,

            isDebuggable =
                (app.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0,

            isEnabled =
                app.enabled,

            riskScore = risk,

            riskLevel = level,

            suspicious = risk >= 60,

            riskReasons = result.reasons,

            recommendUpload = result.recommendUpload,

            recommendation = result.recommendation,

            vtChecked = false,

            vtResult = "",

            scanDate = LocalDate.now().toString(),

            scanTime = LocalTime.now().toString()

        )

        appDao.insert(appInfo)
        val permissions = pkg.requestedPermissions

        permissions?.forEach { permission ->

            val dangerous = when (permission) {

                Manifest.permission.READ_SMS,
                Manifest.permission.SEND_SMS,
                Manifest.permission.RECEIVE_SMS,

                Manifest.permission.READ_CONTACTS,
                Manifest.permission.WRITE_CONTACTS,

                Manifest.permission.READ_CALL_LOG,
                Manifest.permission.WRITE_CALL_LOG,

                Manifest.permission.READ_PHONE_STATE,

                Manifest.permission.RECORD_AUDIO,

                Manifest.permission.CAMERA,

                Manifest.permission.ACCESS_FINE_LOCATION,

                Manifest.permission.ACCESS_COARSE_LOCATION,

                Manifest.permission.SYSTEM_ALERT_WINDOW,

                Manifest.permission.REQUEST_INSTALL_PACKAGES -> true

                else -> false

            }

            permissionDao.insert(

                PermissionInfo(

                    packageName = packageName,

                    permissionName = permission,

                    dangerous = dangerous,

                    granted = true

                )

            )

        }
        if (risk >= 60) {

            suspiciousDao.insert(
                SuspiciousApp(

                    appName = appName,

                    packageName = packageName,

                    sha256 = sha256,

                    riskScore = risk,

                    riskLevel = level,

                    reason = result.reasons,

                    reportDate = LocalDate.now().toString(),

                    sentToServer = false,

                    apkUploaded = false,

                    uploadDate = "",

                    recommendUpload = result.recommendUpload,

                    recommendation = result.recommendation

                )

            )

        }
        app.splitSourceDirs?.forEach { path ->

            try {

                val split = File(path)

                if (!split.exists())
                    return@forEach

                val splitSha256 =
                    HashUtil.sha256(split)

                println(

                    "Split APK : ${split.name}"

                )

                println(

                    splitSha256

                )

            } catch (e: Exception) {

                e.printStackTrace()

            }

        }

    }

}