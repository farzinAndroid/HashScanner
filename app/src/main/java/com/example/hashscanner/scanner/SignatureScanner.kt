package com.example.hashscanner.scanner

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import java.security.MessageDigest

class SignatureScanner(
    private val context: Context
) {

    data class SignatureInfo(
        val sha1: String,
        val sha256: String
    )

    fun getSignature(packageName: String): SignatureInfo {

        val pm = context.packageManager

        return try {

            val signatures =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {

                    val info = pm.getPackageInfo(
                        packageName,
                        PackageManager.GET_SIGNING_CERTIFICATES
                    )

                    info.signingInfo?.apkContentsSigners

                } else {

                    @Suppress("DEPRECATION")
                    val info = pm.getPackageInfo(
                        packageName,
                        PackageManager.GET_SIGNATURES
                    )

                    @Suppress("DEPRECATION")
                    info.signatures

                }

            val cert = signatures?.first()?.toByteArray()

            val sha1 =
                MessageDigest.getInstance("SHA-1")
                    .digest(cert)
                    .joinToString("") {
                        "%02x".format(it)
                    }

            val sha256 =
                MessageDigest.getInstance("SHA-256")
                    .digest(cert)
                    .joinToString("") {
                        "%02x".format(it)
                    }

            SignatureInfo(
                sha1,
                sha256
            )

        } catch (e: Exception) {

            SignatureInfo("", "")

        }

    }

}