package com.example.hashscanner.ui.screens.scan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.hashscanner.R
import com.example.hashscanner.ui.theme.BackgroundColor
import com.example.hashscanner.ui.theme.HashScannerTheme
import com.example.hashscanner.ui.theme.WhitePurple
import com.example.hashscanner.ui.theme.spacing
import com.example.hashscanner.viewmodel.ScannerViewmodel

@Composable
fun ScanningProgressSection(
    paddingValues: PaddingValues,
    scannerViewmodel: ScannerViewmodel = hiltViewModel()
) {

    var progress by remember { mutableFloatStateOf(0f) }
    var percentage by remember { mutableIntStateOf(0) }

    val totalCount by scannerViewmodel.totalCount.collectAsStateWithLifecycle()
    val suspiciousCount by scannerViewmodel.suspiciousCount.collectAsStateWithLifecycle()
    val scannedCount by scannerViewmodel.scannedCount.collectAsStateWithLifecycle()
    val remainingCount by scannerViewmodel.remainingCount.collectAsStateWithLifecycle()
    val appName by scannerViewmodel.appName.collectAsStateWithLifecycle()
    val icon by scannerViewmodel.iconBitmap.collectAsStateWithLifecycle()


    LaunchedEffect(true) {
        scannerViewmodel.startScan()
    }

    LaunchedEffect(scannedCount) {
        progress = if (totalCount > 0) scannedCount.toFloat() / totalCount.toFloat() else 0f
        percentage = (progress * 100).toInt()

//        Log.e("TAG",progress.toString())
//        Log.e("TAG",percentage.toString())
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.BackgroundColor),
        contentPadding = paddingValues,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            Spacer(Modifier.height(MaterialTheme.spacing.dp16))

            Text(
                text = stringResource(R.string.scan_progress_status_scanning_installed_apps),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.WhitePurple,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.dp16))
        }

        item {
            ScanCircleProgress(
                progress = progress,
                percentage = percentage
            )
        }

        item {
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.dp32))

            CurrentlyScanSection(
                appName = appName,
                icon = icon,
                progress = progress,
                scannedCount = scannedCount,
                totalCount = totalCount
            )
        }

        item {
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.dp32))

            ScanProgressionReportBox(
                totalCount = totalCount,
                scannedCount = scannedCount,
                suspiciousCount = suspiciousCount,
                remainingCount = remainingCount
            )
        }

        item {
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.dp32))

            ScanWarning()

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.dp32))
        }
    }
}















@Preview(showBackground = true)
@Composable
fun ScanningProgressSectionPreview() {
    HashScannerTheme {
        ScanningProgressSection(PaddingValues())
    }
}