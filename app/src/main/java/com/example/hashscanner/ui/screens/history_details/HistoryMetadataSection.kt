package com.example.hashscanner.ui.screens.history_details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.hashscanner.R
import com.example.hashscanner.data.model.db_entities.ScanHistory
import com.example.hashscanner.ui.theme.BlackWhiteColor
import com.example.hashscanner.ui.theme.BoxGrayColor
import com.example.hashscanner.utils.DigitHelper

@Composable
fun HistoryMetadataSection(scan: ScanHistory) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
    ) {
        Text(
            text = stringResource(R.string.label_details),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.BlackWhiteColor,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.BoxGrayColor.copy(alpha = 0.5f),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                HistoryMetadataRow(
                    label = stringResource(R.string.report_label_user_apps_count),
                    value = DigitHelper.digitByLang(scan.userApps.toString()),
                    icon = Icons.Default.Person
                )
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f)
                )
                HistoryMetadataRow(
                    label = stringResource(R.string.report_label_system_apps_count),
                    value = DigitHelper.digitByLang(scan.systemApps.toString()),
                    icon = Icons.Default.Settings
                )
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f)
                )
                HistoryMetadataRow(
                    label = stringResource(R.string.report_label_scan_time),
                    value = "${DigitHelper.digitByLang((scan.duration / 1000).toString())} ${stringResource(R.string.label_seconds)}",
                    icon = Icons.Default.Info
                )
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}
