package com.example.hashscanner.data.scanner

import android.Manifest
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import androidx.core.graphics.drawable.toBitmap
import com.example.hashscanner.data.analyzer.RiskAnalyzer
import com.example.hashscanner.data.crypto.HashUtil
import com.example.hashscanner.data.database.dao.AppDao
import com.example.hashscanner.data.database.dao.PermissionDao
import com.example.hashscanner.data.database.dao.SuspiciousDao
import com.example.hashscanner.data.model.db_entities.AppInfo
import com.example.hashscanner.data.model.db_entities.PermissionInfo
import com.example.hashscanner.data.model.db_entities.SuspiciousApp
import com.example.hashscanner.utils.DateTimeUtils
import com.example.hashscanner.utils.IconConverter
import java.io.File
import javax.inject.Inject

class PackageScanner @Inject constructor(
    private val appDao: AppDao,
    private val permissionDao: PermissionDao,
    private val suspiciousDao: SuspiciousDao,
    private val pm: PackageManager,
    private val signatureScanner: SignatureScanner,
    private val certificateScanner: CertificateScanner,
    private val riskAnalyzer: RiskAnalyzer,
) {


    suspend fun startScan(
        onProgress: (scanned: Int, total: Int, suspicious: Int, remaining: Int, appName: String, icon: Bitmap) -> Unit
    ) {


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

        val totalApps = packages.size
        var scannedCount = 0
        var suspiciousCount = 0
        var iconBitmap : Bitmap? = null

        for (pkg in packages) {

            val appName = pkg.applicationInfo?.let { pm.getApplicationLabel(it).toString() }
                ?: pkg.packageName

            try {

                iconBitmap = try {
                    val drawable = pkg?.let { pm.getApplicationIcon(it.applicationInfo!!) }
                        ?: pm.defaultActivityIcon
                    drawable.toBitmap() // Converts Android Drawable to a standard Bitmap
                } catch (e: Exception) {
                    null // If an app has a corrupted icon, we safely ignore it
                }

                val isSuspicious = scanPackage(pkg, iconBitmap)

                scannedCount++
                if (isSuspicious) suspiciousCount++

                val remaining = totalApps - scannedCount
                onProgress(
                    scannedCount,
                    totalApps,
                    suspiciousCount,
                    remaining,
                    appName,
                    iconBitmap!!
                )

            } catch (e: Exception) {

                e.printStackTrace()

                scannedCount++
                val remaining = totalApps - scannedCount
                onProgress(
                    scannedCount,
                    totalApps,
                    suspiciousCount,
                    remaining,
                    appName,
                    iconBitmap!!
                )

            }

        }

    }

    private suspend fun scanPackage(
        pkg: PackageInfo,
        icon: Bitmap? = null
    ): Boolean {

        val app = pkg.applicationInfo ?: return false

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
            return false

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


        val result = riskAnalyzer.analyze(

            pkg,

            app,

            installer

        )

        val risk = result.score

        val level = result.level

        val signature = signatureScanner.getSignature(packageName)
        val certificate = certificateScanner.getCertificate(packageName)


        val appInfo = AppInfo(

            appName = appName,

            iconData = IconConverter.bitmapToByteArray(icon),

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

            scanDate = DateTimeUtils.getCurrentDate(),

            scanTime = DateTimeUtils.getCurrentTime()

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

                    apkPath = apk.absolutePath,

                    riskScore = risk,

                    riskLevel = level,

                    reason = result.reasons,

                    reportDate = DateTimeUtils.getCurrentDate(),

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

        return risk >= 60

    }

}
