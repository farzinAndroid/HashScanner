package com.hashscanner.hashscanner.ui.screens.scan_details

import com.hashscanner.hashscanner.R
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hashscanner.hashscanner.data.model.db_entities.ScanHistory
import com.hashscanner.hashscanner.ui.theme.BlackWhiteColor
import com.hashscanner.hashscanner.ui.theme.BoxGrayColor
import com.hashscanner.hashscanner.utils.DigitHelper

@Composable
fun ScanDetailDataSection(
    scan: ScanHistory,
    serverSusAppCount: Int? = null,
    serverSafeAppCount: Int? = null,
    serverVirusAppCount: Int? = null,
) {

    val isServerSafeAppNull = serverSafeAppCount == null
    val isServerSusAppsNull = serverSusAppCount == null
    val isServerVirusAppsNull = serverVirusAppCount == null


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
                ScanDetailDataRow(
                    label = stringResource(R.string.report_label_user_apps_count),
                    value = DigitHelper.digitByLang(scan.userApps.toString()),
                    icon = Icons.Default.Person
                )
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f)
                )
                ScanDetailDataRow(
                    label = stringResource(R.string.report_label_system_apps_count),
                    value = DigitHelper.digitByLang(scan.systemApps.toString()),
                    icon = Icons.Default.Settings
                )
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f)
                )
                ScanDetailDataRow(
                    label = if (isServerSusAppsNull && isServerVirusAppsNull){
                        "${stringResource(R.string.report_label_suspicious_apps)} (${stringResource(R.string.status_offline)})"
                    }else{
                        "${stringResource(R.string.report_label_suspicious_apps)} (${stringResource(R.string.status_server_verified)})"
                    },
                    value = if (isServerSafeAppNull && isServerSusAppsNull) {
                        DigitHelper.digitByLang(
                            (
                                    scan.lowRisk +
                                            scan.mediumRisk +
                                            scan.highRisk +
                                            scan.criticalRisk).toString()
                        )
                    } else {
                        DigitHelper.digitByLang(
                            (
                                    serverSusAppCount!! +
                                            serverVirusAppCount!!
                                    ).toString()
                        )
                    },
                    icon = Icons.Default.Warning
                )
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f)
                )
                ScanDetailDataRow(
                    label = if (isServerSusAppsNull && isServerVirusAppsNull){
                        "${stringResource(R.string.report_label_safe_apps)} (${stringResource(R.string.status_offline)})"
                    }else{
                        "${stringResource(R.string.report_label_safe_apps)} (${stringResource(R.string.status_server_verified)})"
                    },
                    value = if (isServerSafeAppNull) {
                        DigitHelper.digitByLang((scan.safeApps).toString())
                    } else {
                        DigitHelper.digitByLang((serverSafeAppCount).toString())
                    },
                    icon = Icons.Default.ThumbUp
                )
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f)
                )
                ScanDetailDataRow(
                    label = stringResource(R.string.report_label_scan_time),
                    value = "${DigitHelper.digitByLang((scan.duration / 1000).toString())} ${
                        stringResource(
                            R.string.label_seconds
                        )
                    }",
                    icon = Icons.Default.Info
                )
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}
