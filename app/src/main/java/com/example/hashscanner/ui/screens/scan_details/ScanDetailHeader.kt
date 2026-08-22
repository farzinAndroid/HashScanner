package com.example.hashscanner.ui.screens.scan_details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.hashscanner.R
import com.example.hashscanner.data.model.db_entities.ScanHistory
import com.example.hashscanner.ui.theme.*
import com.example.hashscanner.utils.DigitHelper

@Composable
fun ScanDetailHeader(
    scan: ScanHistory,
    serverSusAppCount: Int? = null,
    serverVirusAppCount: Int? = null,
) {
    val isServerSusAppsNull = serverSusAppCount == null
    val isServerVirusAppsNull = serverVirusAppCount == null

    val totalSuspicious = if (isServerSusAppsNull && isServerVirusAppsNull) {
        scan.highRisk + scan.criticalRisk
    } else {
        (serverSusAppCount ?: 0) + (serverVirusAppCount ?: 0)
    }

    val resultColor = if (totalSuspicious > 0) MaterialTheme.colorScheme.RedColor else MaterialTheme.colorScheme.GreenColor

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(contentAlignment = Alignment.Center) {
            Surface(
                modifier = Modifier.size(140.dp),
                shape = CircleShape,
                color = resultColor.copy(alpha = 0.08f)
            ) {}
            Surface(
                modifier = Modifier.size(110.dp),
                shape = CircleShape,
                color = resultColor.copy(alpha = 0.12f)
            ) {}
            Icon(
                imageVector = if (totalSuspicious > 0) Icons.Default.Warning else Icons.Default.CheckCircle,
                contentDescription = null,
                modifier = Modifier.size(70.dp),
                tint = resultColor
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        val titleText = if (isServerSusAppsNull && isServerVirusAppsNull) {
            if (totalSuspicious > 0) 
                "${stringResource(R.string.topbar_title_scan_result)} (${stringResource(R.string.status_offline)})"
            else 
                stringResource(R.string.result_header_scan_completed_successfully)
        } else {
            if (totalSuspicious > 0) 
                "${stringResource(R.string.topbar_title_scan_result)} (${stringResource(R.string.status_server_verified)})"
            else 
                "${stringResource(R.string.result_header_scan_completed_successfully)} (${stringResource(R.string.status_server_verified)})"
        }

        Text(
            text = titleText,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.BlackWhiteColor,
            fontWeight = FontWeight.ExtraBold
        )

        Text(
            text = "${DigitHelper.digitByLang(scan.scanDate)} - ${DigitHelper.digitByLang(scan.scanTime)}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}
