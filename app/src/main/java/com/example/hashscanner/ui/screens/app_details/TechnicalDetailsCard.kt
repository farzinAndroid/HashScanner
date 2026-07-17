package com.example.hashscanner.ui.screens.app_details

import android.text.format.Formatter
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.hashscanner.R
import com.example.hashscanner.data.model.db_entities.AppInfo
import com.example.hashscanner.ui.theme.BlueColor
import com.example.hashscanner.ui.theme.BoxGrayColor
import com.example.hashscanner.ui.theme.GreenColor
import com.example.hashscanner.ui.theme.RedColor
import com.example.hashscanner.ui.theme.spacing
import com.example.hashscanner.utils.DigitHelper

@Composable
fun TechnicalDetailsCard(appInfo: AppInfo) {
    val context = LocalContext.current
    val formattedSize = Formatter.formatShortFileSize(context, appInfo.apkSize)

    InfoCard(contentPadding = PaddingValues(0.dp)) {
        DetailRow(
            icon = { Icon(Icons.Default.Settings, contentDescription = null) },
            label = stringResource(R.string.details_label_app_version),
            value = DigitHelper.digitByLang(appInfo.versionName)
        )

        HorizontalDivider(
            Modifier,
            DividerDefaults.Thickness,
            color = MaterialTheme.colorScheme.BoxGrayColor
        )

        DetailRow(
            icon = { Icon(Icons.Default.DateRange, contentDescription = null) },
            label = stringResource(R.string.details_label_install_date),
            value = DigitHelper.digitByLang(appInfo.scanDate + " - " + appInfo.scanTime)
        )

        HorizontalDivider(
            Modifier,
            DividerDefaults.Thickness,
            color = MaterialTheme.colorScheme.BoxGrayColor
        )

        DetailRow(
            icon = { Icon(Icons.Default.Info, contentDescription = null) },
            label = stringResource(R.string.details_label_app_size),
            value = DigitHelper.digitByLang(formattedSize)
        )

        HorizontalDivider(
            Modifier,
            DividerDefaults.Thickness,
            color = MaterialTheme.colorScheme.BoxGrayColor
        )

        DetailRow(
            icon = { Icon(Icons.Default.Lock, contentDescription = null) },
            label = stringResource(R.string.details_label_digital_signature_status),
            valueComposable = {


                val (statusText, statusColor, statusIcon) = when {
                    appInfo.isSystem -> {
                        Triple(stringResource(R.string.details_value_signature_system), MaterialTheme.colorScheme.GreenColor, "✓")
                    }
                    listOf("com.android.vending", "com.farsitel.bazaar", "ir.mservices.market").contains(appInfo.installer) -> {
                        Triple(stringResource(R.string.details_value_signature_store), MaterialTheme.colorScheme.GreenColor, "✓")
                    }
                    appInfo.certificateIssuer.contains("Google", ignoreCase = true) || 
                    appInfo.certificateIssuer.contains("Android", ignoreCase = true) -> {
                        Triple(stringResource(R.string.details_value_signature_valid), MaterialTheme.colorScheme.GreenColor, "✓")
                    }
                    appInfo.certificateSha256.isNotBlank() -> {
                        Triple(stringResource(R.string.details_value_signature_sideloaded), MaterialTheme.colorScheme.BlueColor, "ℹ")
                    }
                    else -> {
                        Triple(stringResource(R.string.details_value_signature_unknown), MaterialTheme.colorScheme.RedColor, "✕")
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.dp6)
                ) {
                    Text(
                        text = "$statusIcon $statusText",
                        color = statusColor,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        )

        if (appInfo.certificateIssuer.isNotBlank()) {
            HorizontalDivider(
                Modifier,
                DividerDefaults.Thickness,
                color = MaterialTheme.colorScheme.BoxGrayColor
            )
            DetailRow(
                icon = { Icon(Icons.Default.Info, contentDescription = null) },
                label = "صادرکننده",
                value = appInfo.certificateIssuer.substringBefore(",").substringAfter("CN=")
            )
        }
    }
}


