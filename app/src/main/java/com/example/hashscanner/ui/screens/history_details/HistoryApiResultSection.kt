package com.example.hashscanner.ui.screens.history_details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hashscanner.data.model.api.ApiAppResult
import com.example.hashscanner.data.model.api.ScanResultResponse
import com.example.hashscanner.data.model.api.ScanSummary
import com.example.hashscanner.data.network.NetworkResult
import com.example.hashscanner.ui.theme.BlackWhiteColor
import com.example.hashscanner.ui.theme.BoxGrayColor
import com.example.hashscanner.ui.theme.GreenColor
import com.example.hashscanner.ui.theme.HashScannerTheme
import com.example.hashscanner.ui.theme.RedColor
import com.example.hashscanner.ui.theme.StrongYellowColor
import com.example.hashscanner.utils.DigitHelper

@Composable
fun HistoryApiResultSection(
    scanResult: NetworkResult<ScanResultResponse>,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(visible = scanResult is NetworkResult.Success) {
        if (scanResult is NetworkResult.Success) {
            val data = scanResult.data ?: return@AnimatedVisibility
            if (!data.ready) return@AnimatedVisibility

            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Summary Card
                data.summary?.let { ApiSummaryCard(it) }

                // Affected Apps
                data.apps?.let { apps ->
                    if (apps.isNotEmpty()) {
                        Text(
                            text = "نتایج تحلیل برنامه‌ها",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.BlackWhiteColor,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        apps.forEach { app ->
                            ApiAppResultCard(app)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HistoryApiResultSectionPreview() {
    HashScannerTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            HistoryApiResultSection(
                scanResult = NetworkResult.Success(
                    message = "Analysis complete",
                    data = ScanResultResponse(
                        ready = true,
                        summary = ScanSummary(
                            result = "VIRUS",
                            message = "تعداد 1 برنامه آلوده شناسایی شد."
                        ),
                        apps = listOf(
                            ApiAppResult(
                                appName = "Example Malicious App",
                                packageName = "com.malicious.example",
                                sha256 = "SHA256_HASH_HERE",
                                riskScore = 95,
                                riskLevel = "CRITICAL",
                                reason = "Suspicious behavior and multiple dangerous permissions",
                                result = "VIRUS",
                                action = "DELETE",
                                message = "این برنامه یک بدافزار شناخته شده است. پیشنهاد می‌شود فورا آن را حذف کنید."
                            )
                        )
                    )
                )
            )
        }
    }
}
