package com.example.hashscanner.ui.screens.scan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.hashscanner.ui.theme.BackgroundColor
import com.example.hashscanner.viewmodel.ScannerViewmodel

@Composable
fun ScanCompleteSection(
    paddingValues: PaddingValues,
    scannerViewmodel: ScannerViewmodel = hiltViewModel()
) {

    val totalCount by scannerViewmodel.totalCount.collectAsStateWithLifecycle()
    val suspiciousCount by scannerViewmodel.suspiciousCount.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(MaterialTheme.colorScheme.BackgroundColor)
    ) {

        ScanDoneShield(
            modifier = Modifier
                .weight(0.3f)
        )

        ReportSection(
            modifier = Modifier
                .weight(0.7f),
            totalApps = totalCount,
            suspiciousApps = suspiciousCount,
            onButtonClick = {}
        )
    }

}