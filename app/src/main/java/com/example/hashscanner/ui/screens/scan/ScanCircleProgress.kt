package com.example.hashscanner.ui.screens.scan

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.hashscanner.R
import com.example.hashscanner.ui.theme.AccentPurpleColor
import com.example.hashscanner.ui.theme.PrimaryTextColor

@Composable
fun ScanCircleProgress() {
    Box(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
    ){


        Text(
            text = "30%",
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.AccentPurpleColor,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            textAlign = TextAlign.Center
        )


        CircularProgressIndicator(
            progress = { 0.4f },
            trackColor = MaterialTheme.colorScheme.AccentPurpleColor,
            gapSize = 0.dp,
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.Center),
            color = MaterialTheme.colorScheme.PrimaryTextColor
        )
    }
}