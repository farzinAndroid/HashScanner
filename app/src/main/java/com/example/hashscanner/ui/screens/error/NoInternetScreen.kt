package com.example.hashscanner.ui.screens.error

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.hashscanner.R
import com.example.hashscanner.ui.theme.AccentPurpleColor
import com.example.hashscanner.ui.theme.BlackWhiteColor
import com.example.hashscanner.ui.theme.Typography
import com.example.hashscanner.ui.theme.spacing
import com.example.hashscanner.ui.ui_utils.MainPurpleButton

@Composable
fun NoInternetScreen(
    isChecking: Boolean,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(MaterialTheme.spacing.dp24),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (isChecking) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.AccentPurpleColor,
                modifier = Modifier.size(64.dp)
            )
        } else {
            Image(
                painter = painterResource(R.drawable.cloud),
                contentDescription = null,
                modifier = Modifier.size(150.dp),
                contentScale = ContentScale.Fit
            )
        }

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.dp32))

        Text(
            text = if (isChecking) stringResource(R.string.status_checking) else stringResource(R.string.no_internet_title),
            style = Typography.headlineSmall,
            color = MaterialTheme.colorScheme.BlackWhiteColor,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.dp8))

        Text(
            text = if (isChecking) stringResource(R.string.status_please_wait) else stringResource(R.string.no_internet_description),
            style = Typography.bodyMedium,
            color = MaterialTheme.colorScheme.BlackWhiteColor,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.dp32))

        MainPurpleButton(
            text = stringResource(R.string.button_retry),
            onClick = onRetry,
            modifier = Modifier.padding(horizontal = 48.dp),
            enabled = !isChecking
        )
    }
}
