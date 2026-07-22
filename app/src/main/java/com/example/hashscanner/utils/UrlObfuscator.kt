package com.example.hashscanner.utils

import android.util.Base64
import com.example.hashscanner.BuildConfig

object UrlObfuscator {

    /**
     * Decodes the obfuscated Report URL from BuildConfig.
     */
    fun getReportBaseUrl(): String {
        return try {
            val encodedUrl = BuildConfig.ENCODED_REPORT_BASE_URL
            if (encodedUrl.isBlank()) {
                android.util.Log.e("UrlObfuscator", "Encoded Report URL is blank!")
                return ""
            }
            
            val decodedBytes = Base64.decode(encodedUrl, Base64.DEFAULT)
            val url = String(decodedBytes, Charsets.UTF_8).replace("\"", "")
            
            android.util.Log.d("UrlObfuscator", "Decoded Report URL: $url")
            url
        } catch (e: Exception) {
            android.util.Log.e("UrlObfuscator", "Error decoding Report URL", e)
            ""
        }
    }

    /**
     * Decodes the obfuscated APK URL from BuildConfig.
     */
    fun getApkBaseUrl(): String {
        return try {
            val encodedUrl = BuildConfig.ENCODED_APK_BASE_URL
            if (encodedUrl.isBlank()) {
                android.util.Log.e("UrlObfuscator", "Encoded APK URL is blank!")
                return ""
            }

            val decodedBytes = Base64.decode(encodedUrl, Base64.DEFAULT)
            val url = String(decodedBytes, Charsets.UTF_8).replace("\"", "")

            android.util.Log.d("UrlObfuscator", "Decoded APK URL: $url")
            url
        } catch (e: Exception) {
            android.util.Log.e("UrlObfuscator", "Error decoding APK URL", e)
            ""
        }
    }
}
