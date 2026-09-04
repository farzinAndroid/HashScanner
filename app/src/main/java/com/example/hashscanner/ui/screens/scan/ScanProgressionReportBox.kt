package com.example.hashscanner.ui.screens.scan

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.hashscanner.R
import com.example.hashscanner.ui.theme.BoxGrayColor
import com.example.hashscanner.ui.theme.spacing
import com.example.hashscanner.ui.ui_utils.InfoCard

@Composable
fun ScanProgressionReportBox(
    totalCount: Int,
    scannedCount: Int,
    suspiciousCount: Int,
    remainingCount: Int
) {


    InfoCard(
        modifier = Modifier.padding(MaterialTheme.spacing.dp16)
    ) {

        ProgressReportRow(
            title = stringResource(R.string.scan_statistic_total_apps_count),
            counter = totalCount
        )
        HorizontalDivider(
            Modifier,
            DividerDefaults.Thickness,
            color = MaterialTheme.colorScheme.BoxGrayColor
        )
        ProgressReportRow(
            title = stringResource(R.string.scan_statistic_scanned_apps_count),
            counter = scannedCount
        )
        HorizontalDivider(
            Modifier,
            DividerDefaults.Thickness,
            color = MaterialTheme.colorScheme.BoxGrayColor
        )
        ProgressReportRow(
            title = stringResource(R.string.scan_statistic_suspicious_apps_count),
            counter = suspiciousCount
        )
        HorizontalDivider(
            Modifier,
            DividerDefaults.Thickness,
            color = MaterialTheme.colorScheme.BoxGrayColor
        )
        ProgressReportRow(
            title = stringResource(R.string.scan_statistic_remaining_apps_count),
            counter = remainingCount
        )


    }

}