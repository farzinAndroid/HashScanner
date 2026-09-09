package com.hashscanner.hashscanner.repository

import android.graphics.Bitmap
import com.hashscanner.hashscanner.data.scanner.PackageScanner
import javax.inject.Inject

class ScannerRepository @Inject constructor(
    private val packageScanner: PackageScanner
){

    suspend fun startScan(
        scanId: String,
        onProgress: (scanned: Int, total: Int, suspicious: Int, remaining: Int, appName: String,iconBitmap: Bitmap) -> Unit
    ) = packageScanner.startScan(scanId = scanId, onProgress = onProgress)

}
