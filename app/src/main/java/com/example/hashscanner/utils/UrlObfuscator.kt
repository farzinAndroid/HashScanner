package com.example.hashscanner.utils

import android.util.Base64
import com.example.hashscanner.BuildConfig

object UrlObfuscator {

    /**
     * Decodes the obfuscated URL from BuildConfig.
     * This makes it harder for simple static analysis to find the URL in the APK.
     */
    fun getBaseUrl(): String {
        return try {
            val encodedUrl = BuildConfig.ENCODED_BASE_URL
            if (encodedUrl.isBlank()) {
                android.util.Log.e("UrlObfuscator", "Encoded URL is blank!")
                return ""
            }
            
            val decodedBytes = Base64.decode(encodedUrl, Base64.DEFAULT)
            val url = String(decodedBytes, Charsets.UTF_8).replace("\"", "")
            
            android.util.Log.d("UrlObfuscator", "Decoded URL: $url")
            url
        } catch (e: Exception) {
            android.util.Log.e("UrlObfuscator", "Error decoding URL", e)
            ""
        }
    }
}
