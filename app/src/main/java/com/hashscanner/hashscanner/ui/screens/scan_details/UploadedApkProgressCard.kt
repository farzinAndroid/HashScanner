package com.hashscanner.hashscanner.ui.screens.scan_details

import com.hashscanner.hashscanner.R
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hashscanner.hashscanner.ui.theme.BlackWhiteColor
import com.hashscanner.hashscanner.ui.theme.BoxGrayColor
import com.hashscanner.hashscanner.ui.theme.HashScannerTheme
import com.hashscanner.hashscanner.ui.theme.LightGray
import com.hashscanner.hashscanner.ui.theme.StrongYellowColor

@Composable
fun UploadedApkProgressCard(
    total: Int,
    checked: Int,
    pending: Int
) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.BoxGrayColor,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.StrongYellowColor.copy(alpha = 0.3f)),
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.StrongYellowColor
                )
                Text(
                    text = stringResource(R.string.report_label_uploaded_apks),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.BlackWhiteColor
                )
            }

            Text(
                text = stringResource(R.string.report_uploaded_analysis_in_progress, total, pending),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.LightGray
            )

            val progress = if (total > 0) checked.toFloat() / total else 0f
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth().height(6.dp),
                color = MaterialTheme.colorScheme.StrongYellowColor,
                trackColor = MaterialTheme.colorScheme.StrongYellowColor.copy(alpha = 0.1f),
                strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
            )
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun UploadedApkProgressCardPreview() {
    HashScannerTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            UploadedApkProgressCard(
                total = 5,
                checked = 2,
                pending = 3
            )
        }
    }
}
