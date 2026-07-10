package com.example.hashscanner.data.scanner

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import java.io.ByteArrayInputStream
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate

class CertificateScanner(

    private val context: Context

) {

    data class CertificateInfo(

        val issuer: String,

        val subject: String,

        val serial: String,

        val validFrom: Long,

        val validTo: Long,

        val algorithm: String

    )

    fun getCertificate(

        packageName: String

    ): CertificateInfo {

        val pm = context.packageManager

        return try {

            val signatures =

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {

                    pm.getPackageInfo(

                        packageName,

                        PackageManager.GET_SIGNING_CERTIFICATES

                    ).signingInfo?.apkContentsSigners

                } else {

                    @Suppress("DEPRECATION")

                    pm.getPackageInfo(

                        packageName,

                        PackageManager.GET_SIGNATURES

                    ).signatures

                }

            val cf =

                CertificateFactory.getInstance("X.509")

            val cert =

                cf.generateCertificate(

                    ByteArrayInputStream(

                        signatures?.first()?.toByteArray()

                    )

                ) as X509Certificate

            CertificateInfo(

                issuer = cert.issuerX500Principal.name,

subject = cert.subjectX500Principal.name,

                serial = cert.serialNumber.toString(),

                validFrom = cert.notBefore.time,

                validTo = cert.notAfter.time,

                algorithm = cert.sigAlgName

            )

        } catch (e: Exception) {

            CertificateInfo(

    "",

    "",

    "",

    0L,

    0L,

    ""

)

        }

    }

}