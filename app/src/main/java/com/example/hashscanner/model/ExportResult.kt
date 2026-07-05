package com.example.hashscanner.model

import java.io.File

data class ExportResult(
    val pdf: File? = null,
    val json: File? = null,
    val csv: File? = null,
    val database: File? = null,
    val zip: File? = null,
    val errors: List<String> = emptyList()
)