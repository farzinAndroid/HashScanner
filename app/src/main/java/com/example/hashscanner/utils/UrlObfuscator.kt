package com.example.hashscanner.utils

import android.util.Base64
import android.util.Log
import com.example.hashscanner.BuildConfig

object UrlObfuscator {

    /**
     * Decodes the obfuscated BASE URL from BuildConfig.
     */
    fun getBaseUrl(): String {
        return try {
            val encodedUrl = BuildConfig.BASE_URL
            if (encodedUrl.isBlank()) {
                Log.e(Constants.TAG, "Encoded Report URL is blank!")
                return ""
            }
            
            val decodedBytes = Base64.decode(encodedUrl, Base64.DEFAULT)
            val url = String(decodedBytes, Charsets.UTF_8).replace("\"", "")
            
            Log.d(Constants.TAG, "Decoded Report URL: $url")
            url
        } catch (e: Exception) {
            Log.e(Constants.TAG, "Error decoding Report URL", e)
            ""
        }
    }
}
