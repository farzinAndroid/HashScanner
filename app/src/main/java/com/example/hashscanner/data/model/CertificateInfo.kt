package com.example.hashscanner.data.model

data class CertificateInfo(

        val issuer: String,

        val subject: String,

        val serial: String,

        val validFrom: Long,

        val validTo: Long,

        val algorithm: String

    )