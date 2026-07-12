package com.example.hashscanner.ui.screens.scan

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.hashscanner.R
import com.example.hashscanner.ui.theme.BoxGrayColor
import com.example.hashscanner.ui.theme.LightGray
import com.example.hashscanner.ui.theme.spacing

@Composable
fun ScanProgressionReportBox(
    totalCount: Int,
    scannedCount: Int,
    suspiciousCount: Int,
    remainingCount: Int
) {


    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.spacing.dp16)
            .wrapContentHeight()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.LightGray,
                shape = Shapes().large
            ),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.BoxGrayColor,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp,
        ),

    ) {

        ProgressReportRow(
            title = stringResource(R.string.scan_statistic_total_apps_count),
            counter = totalCount
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(MaterialTheme.colorScheme.LightGray)
        )
        ProgressReportRow(
            title = stringResource(R.string.scan_statistic_scanned_apps_count),
            counter = scannedCount
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(MaterialTheme.colorScheme.LightGray)
        )
        ProgressReportRow(
            title = stringResource(R.string.scan_statistic_suspicious_apps_count),
            counter = suspiciousCount
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(MaterialTheme.colorScheme.LightGray)
        )
        ProgressReportRow(
            title = stringResource(R.string.scan_statistic_remaining_apps_count),
            counter = remainingCount
        )


    }

}