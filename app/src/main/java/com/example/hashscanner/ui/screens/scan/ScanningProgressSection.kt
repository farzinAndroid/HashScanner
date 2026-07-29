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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.hashscanner.R
import com.example.hashscanner.ui.theme.BackgroundColor
import com.example.hashscanner.ui.theme.HashScannerTheme
import com.example.hashscanner.ui.theme.WhitePurple
import com.example.hashscanner.ui.theme.spacing
import com.example.hashscanner.viewmodel.ScannerViewModel

@Composable
fun ScanningProgressSection(
    paddingValues: PaddingValues,
    scannerViewModel: ScannerViewModel = hiltViewModel()
) {

    var progress by remember { mutableFloatStateOf(0f) }
    
    val totalCount by scannerViewModel.totalCount.collectAsStateWithLifecycle()
    val suspiciousCount by scannerViewModel.suspiciousCount.collectAsStateWithLifecycle()
    val scannedCount by scannerViewModel.scannedCount.collectAsStateWithLifecycle()
    val remainingCount by scannerViewModel.remainingCount.collectAsStateWithLifecycle()
    val appName by scannerViewModel.appName.collectAsStateWithLifecycle()
    val icon by scannerViewModel.iconBitmap.collectAsStateWithLifecycle()

    val percentage by remember(scannedCount, totalCount) {
        mutableIntStateOf(if (totalCount > 0) (scannedCount.toFloat() / totalCount.toFloat() * 100).toInt() else 0)
    }

    LaunchedEffect(scannedCount) {
        progress = if (totalCount > 0) scannedCount.toFloat() / totalCount.toFloat() else 0f
    }

    val scanState by scannerViewModel.isScanCompleted.collectAsStateWithLifecycle()


    LaunchedEffect(Unit) {
        if (scanState == ScanPageState.SCANNING) {
            scannerViewModel.startScan()
        }
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
                text = if (scanState == ScanPageState.UPLOADING)
                    stringResource(R.string.scan_progress_status_uploading)
                else
                    stringResource(R.string.scan_progress_status_scanning_installed_apps),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.WhitePurple,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.dp8))
        }

        item {
            ScanCircleProgress(
                progress = if (scanState == ScanPageState.UPLOADING) 1f else progress,
                percentage = if (scanState == ScanPageState.UPLOADING) 100 else percentage
            )
        }

        item {
            if (scanState == ScanPageState.SCANNING) {
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.dp16))

                CurrentlyScanSection(
                    appName = appName,
                    icon = icon,
                    progress = progress,
                    scannedCount = scannedCount,
                    totalCount = totalCount
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.dp16))

            ScanProgressionReportBox(
                totalCount = totalCount,
                scannedCount = scannedCount,
                suspiciousCount = suspiciousCount,
                remainingCount = remainingCount
            )
        }

        item {
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.dp16))

            ScanWarning()

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