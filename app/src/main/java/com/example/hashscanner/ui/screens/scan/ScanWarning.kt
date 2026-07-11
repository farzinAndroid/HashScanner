package com.example.hashscanner.ui.screens.scan

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hashscanner.R
import com.example.hashscanner.ui.theme.HashScannerTheme
import com.example.hashscanner.ui.theme.RedColor
import com.example.hashscanner.ui.theme.RedWhite
import com.example.hashscanner.ui.theme.spacing

@Composable
fun ScanWarning() {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.spacing.dp16)
            .height(56.dp)
            .background(
                shape = Shapes().large,
                color = MaterialTheme.colorScheme.RedColor.copy(0.4f)
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.RedWhite,
                shape = Shapes().large
            ),
        contentAlignment = Alignment.Center

    ) {

        Text(
            text = stringResource(R.string.scan_warning_do_not_close_app),
            modifier = Modifier,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.RedWhite
        )

    }


}

@Preview(showBackground = true)
@Composable
fun ScanWarningPreview() {
    HashScannerTheme {
        ScanWarning()
    }
}