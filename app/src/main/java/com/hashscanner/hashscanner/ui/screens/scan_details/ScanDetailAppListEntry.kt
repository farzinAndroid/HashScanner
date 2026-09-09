package com.hashscanner.hashscanner.ui.screens.scan_details

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hashscanner.hashscanner.R
import com.hashscanner.hashscanner.ui.theme.AccentPurpleColor
import com.hashscanner.hashscanner.ui.theme.BlackWhiteColor
import com.hashscanner.hashscanner.ui.theme.BoxGrayColor
import com.hashscanner.hashscanner.ui.theme.LightGray
import com.hashscanner.hashscanner.utils.DigitHelper

@Composable
fun ScanDetailAppListEntry(
    serverSusAppCount: Int? = null,
    serverVirusAppCount: Int? = null,
    localSusAppCount: Int,
    onClick: () -> Unit
) {
    val totalSus = if (serverSusAppCount == null && serverVirusAppCount == null) {
        localSusAppCount
    } else {
        (serverSusAppCount ?: 0) + (serverVirusAppCount ?: 0)
    }

    val isServer = serverSusAppCount != null || serverVirusAppCount != null

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.BoxGrayColor,
        tonalElevation = 2.dp,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.BoxGrayColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.List,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.AccentPurpleColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Column {
                    Text(
                        text = stringResource(R.string.app_list),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.BlackWhiteColor,
                        fontWeight = FontWeight.Bold
                    )
                    
                    val statusText = if (isServer) {
                        if (totalSus > 0) 
                            "${stringResource(R.string.status_server_check)}: ${DigitHelper.digitByLang(totalSus.toString())} ${stringResource(R.string.status_malware_and_suspicious)}" 
                        else 
                            "${stringResource(R.string.status_server_check)}: ${stringResource(R.string.status_all_apps_safe)}"
                    } else {
                        "${stringResource(R.string.status_offline_check)}: ${DigitHelper.digitByLang(totalSus.toString())} ${stringResource(R.string.status_suspicious_items)}"
                    }

                    Text(
                        text = statusText,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isServer && totalSus > 0) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.LightGray
                    )
                }
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.LightGray
            )
        }
    }
}