package com.example.hashscanner.ui.screens.history_details

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
import com.example.hashscanner.ui.theme.BlackWhiteColor
import com.example.hashscanner.utils.DigitHelper

@Composable
fun HistoryHeader(scan: ScanHistory, resultColor: Color, totalSuspicious: Int) {
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

        Text(
            text = if (totalSuspicious > 0) 
                stringResource(R.string.topbar_title_suspicious_apps) 
            else 
                stringResource(R.string.result_header_scan_completed_successfully),
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
