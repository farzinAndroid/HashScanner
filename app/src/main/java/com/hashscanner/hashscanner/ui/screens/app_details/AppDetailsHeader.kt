package com.hashscanner.hashscanner.ui.screens.app_details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.hashscanner.hashscanner.R
import com.hashscanner.hashscanner.data.model.db_entities.AppInfo
import com.hashscanner.hashscanner.ui.theme.BlackWhiteColor
import com.hashscanner.hashscanner.ui.theme.spacing
import com.hashscanner.hashscanner.utils.IconConverter

@Composable
fun AppHeaderSection(
    appInfo: AppInfo
) {
    val iconBitmap = IconConverter.byteArrayToBitmap(appInfo.iconData)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val iconModifier = Modifier
            .size(64.dp)
            .clip(RoundedCornerShape(MaterialTheme.spacing.dp16))
            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))

        if (iconBitmap != null) {
            Image(
                bitmap = iconBitmap.asImageBitmap(),
                contentDescription = null,
                modifier = iconModifier
            )
        } else {
            Image(
                painter = painterResource(R.drawable.ic_launcher_background),
                contentDescription = null,
                modifier = iconModifier
            )
        }

        Column(
            modifier = Modifier.weight(1f).padding(horizontal = MaterialTheme.spacing.dp16),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = appInfo.appName,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.BlackWhiteColor
            )
            Text(
                text = appInfo.packageName,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                fontWeight = FontWeight.Bold
            )
            val effectiveLevel = when {
                appInfo.isServerVerified -> when (appInfo.serverResult) {
                    "SAFE" -> com.hashscanner.hashscanner.data.model.other.RiskLevels.SAFE.toString()
                    "SUSPICIOUS" -> com.hashscanner.hashscanner.data.model.other.RiskLevels.HIGH.toString()
                    "VIRUS" -> com.hashscanner.hashscanner.data.model.other.RiskLevels.CRITICAL.toString()
                    else -> appInfo.riskLevel
                }
                else -> appInfo.riskLevel
            }
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.dp8))
            RiskBadge(effectiveLevel)
        }
    }
}


