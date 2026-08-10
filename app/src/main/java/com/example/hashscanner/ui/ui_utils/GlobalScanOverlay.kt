package com.example.hashscanner.ui.ui_utils

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.hashscanner.data.model.api.ScanResultResponse
import com.example.hashscanner.data.network.NetworkResult
import com.example.hashscanner.ui.navigation.Screens
import com.example.hashscanner.ui.theme.HashScannerTheme
import com.example.hashscanner.viewmodel.ScannerViewModel

@Composable
fun GlobalScanOverlay(
    scannerViewModel: ScannerViewModel,
    navController: NavHostController
) {
    val scanResult by scannerViewModel.scanResultResponse.collectAsStateWithLifecycle()

    GlobalScanOverlayContent(
        scanResult = scanResult,
        onViewResultsClick = {
            navController.navigate(Screens.RiskLevelList())
        }
    )
}

@Composable
fun GlobalScanOverlayContent(
    scanResult: NetworkResult<ScanResultResponse>,
    onViewResultsClick: () -> Unit
) {
    if (scanResult is NetworkResult.Success) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 32.dp),
            contentAlignment = Alignment.BottomCenter // Floats at the bottom
        ) {
            Card(
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Scan Processing Complete!")
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Button(onClick = onViewResultsClick) {
                        Text("View Results")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GlobalScanOverlayPreview() {
    HashScannerTheme {
        GlobalScanOverlayContent(
            scanResult = NetworkResult.Success(
                message = "Success",
                data = ScanResultResponse(ready = true)
            ),
            onViewResultsClick = {}
        )
    }
}
