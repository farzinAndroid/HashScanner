package com.example.hashscanner.ui.screens.scan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.hashscanner.R
import com.example.hashscanner.ui.theme.BackgroundColor
import com.example.hashscanner.ui.theme.WhitePurple
import com.example.hashscanner.ui.theme.HashScannerTheme
import com.example.hashscanner.ui.theme.spacing

@Composable
fun ScanningProgressSection(
    paddingValues: PaddingValues
) {


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.BackgroundColor)
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {


        Spacer(Modifier.height(MaterialTheme.spacing.dp16))

        Text(
            text = stringResource(R.string.scan_progress_status_scanning_installed_apps),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.WhitePurple,
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.dp16))


        ScanCircleProgress()

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.dp32))

        CurrentlyScanSection()


        Spacer(modifier = Modifier.height(MaterialTheme.spacing.dp32))


        ScanProgressionReportBox()

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.dp32))

        ScanWarning()

    }


}















@Preview(showBackground = true)
@Composable
fun ScanningProgressSectionPreview() {
    HashScannerTheme {
        ScanningProgressSection(PaddingValues())
    }
}