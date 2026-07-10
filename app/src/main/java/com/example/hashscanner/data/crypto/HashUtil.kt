package com.example.hashscanner.data.crypto

import java.io.File
import java.io.FileInputStream
import java.security.MessageDigest

object HashUtil {

    private fun hash(
        file: File,
        algorithm: String
    ): String {

        val digest =
            MessageDigest.getInstance(algorithm)

        FileInputStream(file).use { input ->

            val buffer = ByteArray(1024 * 1024)

            var read: Int

            while (true) {

                read = input.read(buffer)

                if (read == -1)
                    break

                digest.update(buffer, 0, read)
            }
        }

        return digest.digest().joinToString("") {
            "%02x".format(it)
        }

    }

    fun md5(file: File): String {

        return hash(file, "MD5")

    }

    fun sha1(file: File): String {

        return hash(file, "SHA-1")

    }

    fun sha256(file: File): String {

        return hash(file, "SHA-256")

    }

}