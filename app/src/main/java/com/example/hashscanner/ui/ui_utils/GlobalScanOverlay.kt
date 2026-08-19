package com.example.hashscanner.ui.ui_utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.hashscanner.R
import com.example.hashscanner.data.model.api.ScanResultResponse
import com.example.hashscanner.data.network.NetworkResult
import com.example.hashscanner.ui.navigation.Screens
import com.example.hashscanner.ui.theme.AccentPurpleColor
import com.example.hashscanner.ui.theme.BlackWhiteColor
import com.example.hashscanner.ui.theme.HashScannerTheme
import com.example.hashscanner.ui.theme.spacing
import com.example.hashscanner.viewmodel.ScannerViewModel

@Composable
fun GlobalScanOverlay(
    scannerViewModel: ScannerViewModel,
    navController: NavHostController
) {
    val scanResult by scannerViewModel.scanResultNotificationPopUp.collectAsStateWithLifecycle()

    GlobalScanOverlayContent(
        scanResult = scanResult,
        onViewResultsClick = {
            scannerViewModel.clearScanResult()
            navController.navigate(Screens.RiskLevelList())
        },
        onCloseClick = {
            scannerViewModel.clearScanResult()
        }
    )
}

@Composable
fun GlobalScanOverlayContent(
    scanResult: NetworkResult<ScanResultResponse>,
    onViewResultsClick: () -> Unit,
    onCloseClick: () -> Unit
) {
    if (scanResult is NetworkResult.Success) {
        val data = scanResult.data
        val message = data?.finalReport?.message
            ?: data?.initialReport?.let {
                stringResource(R.string.notification_analysis_complete_with_virus, it.summary.virus)
            } ?: data?.message ?: stringResource(R.string.notification_analysis_complete_generic)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(MaterialTheme.spacing.dp16),
            contentAlignment = Alignment.TopCenter // Show at the top like a notification
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                shape = RoundedCornerShape(24.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp,
                shadowElevation = 12.dp
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.AccentPurpleColor.copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.AccentPurpleColor,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = stringResource(R.string.topbar_title_scan_result),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.BlackWhiteColor,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }

                        IconButton(onClick = onCloseClick) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        lineHeight = 20.sp,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    MainPurpleButton(
                        text = stringResource(R.string.button_view_detailed_results),
                        onClick = onViewResultsClick,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Preview(showBackground = false)
@Composable
fun GlobalScanOverlayPreview() {
    HashScannerTheme {
        Box(modifier = Modifier.fillMaxSize().background(Color.Gray.copy(0.2f))) {
            GlobalScanOverlayContent(
                scanResult = NetworkResult.Success(
                    message = "Success",
                    data = ScanResultResponse(
                        ready = true,
                        stage = "INITIAL_READY",
                        message = "تحلیل اسکن پایان یافت."
                    )
                ),
                onViewResultsClick = {},
                onCloseClick = {}
            )
        }
    }
}
