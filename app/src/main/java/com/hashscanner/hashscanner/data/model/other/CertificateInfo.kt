package com.hashscanner.hashscanner.data.model.other

data class CertificateInfo(

        val issuer: String,

        val subject: String,

        val serial: String,

        val validFrom: Long,

        val validTo: Long,

        val algorithm: String

    )