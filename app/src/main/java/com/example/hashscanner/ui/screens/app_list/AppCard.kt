package com.example.hashscanner.ui.screens.app_list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hashscanner.R
import com.example.hashscanner.data.model.db_entities.AppInfo
import com.example.hashscanner.data.model.other.RiskLevels
import com.example.hashscanner.ui.theme.*
import com.example.hashscanner.utils.DigitHelper
import com.example.hashscanner.utils.IconConverter

@Composable
fun AppCard(
    appInfo: AppInfo,
    onClick: () -> Unit
) {
    val iconBitmap = IconConverter.byteArrayToBitmap(appInfo.iconData)

    val riskColor = when (appInfo.riskLevel) {
        RiskLevels.LOW.toString(), RiskLevels.SAFE.toString() -> MaterialTheme.colorScheme.GreenColor
        RiskLevels.MEDIUM.toString() -> MaterialTheme.colorScheme.YellowColor
        RiskLevels.HIGH.toString(), RiskLevels.CRITICAL.toString() -> MaterialTheme.colorScheme.RedColor
        else -> Color.Transparent
    }
    
    val riskBorderColor = when (appInfo.riskLevel) {
        RiskLevels.LOW.toString(), RiskLevels.SAFE.toString() -> MaterialTheme.colorScheme.GreenColor
        RiskLevels.MEDIUM.toString() -> MaterialTheme.colorScheme.StrongYellowColor
        RiskLevels.HIGH.toString(), RiskLevels.CRITICAL.toString() -> MaterialTheme.colorScheme.RedColor
        else -> Color.Transparent
    }

    val riskText = when (appInfo.riskLevel) {
        RiskLevels.LOW.toString() -> stringResource(R.string.badge_risk_level_low)
        RiskLevels.MEDIUM.toString() -> stringResource(R.string.badge_risk_level_medium)
        RiskLevels.HIGH.toString() -> stringResource(R.string.badge_risk_level_high)
        RiskLevels.CRITICAL.toString() -> stringResource(R.string.badge_risk_level_very_high)
        RiskLevels.SAFE.toString() -> stringResource(R.string.badge_risk_level_none)
        else -> ""
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(top = MaterialTheme.spacing.dp8)
            .padding(horizontal = MaterialTheme.spacing.dp20)
            .alpha(if (appInfo.isDeleted) 0.6f else 1f),
        colors = CardDefaults.cardColors(
            containerColor = riskColor.copy(0.3f)
        ),
        onClick = onClick,
        border = BorderStroke(
            width = 1.dp,
            color = if (appInfo.isDeleted) MaterialTheme.colorScheme.outline.copy(0.5f) else riskBorderColor
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = MaterialTheme.spacing.dp16),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val iconModifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .align(Alignment.CenterVertically)

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
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(start = MaterialTheme.spacing.dp8)
                        .padding(vertical = MaterialTheme.spacing.dp8)
                        .weight(1f)
                ) {
                    Text(
                        text = appInfo.appName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.BlackWhiteColor,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = appInfo.packageName,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.BlackWhiteColor,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )

                    Text(
                        text = stringResource(R.string.format_risk_score_value) +
                                DigitHelper.digitByLang(appInfo.riskScore.toString()) +
                                " ${stringResource(R.string.label_out_of_100)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.BlackWhiteColor,
                        modifier = Modifier.padding(top = MaterialTheme.spacing.dp8),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }

                RiskBadge(
                    riskText = riskText,
                    color = riskBorderColor
                )
            }

            if (appInfo.isDeleted) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(top = 8.dp, start = 8.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = stringResource(R.string.status_uninstalled),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppCardPreview() {
    HashScannerTheme {
        AppCard(
            appInfo = AppInfo(
                appName = "Sample App",
                packageName = "com.example.sample",
                versionName = "1.0.0",
                versionCode = 1L,
                apkName = "sample.apk",
                apkPath = "/data/app/sample.apk",
                apkSize = 1024L,
                md5 = "md5",
                sha1 = "sha1",
                sha256 = "sha256",
                certificateSha1 = "cert_sha1",
                certificateSha256 = "cert_sha256",
                certificateIssuer = "issuer",
                certificateSubject = "subject",
                certificateSerial = "serial",
                certificateAlgorithm = "algorithm",
                certificateValidFrom = "0",
                certificateValidTo = "0",
                installer = "installer",
                firstInstallTime = 0L,
                lastUpdateTime = 0L,
                targetSdk = 33,
                minSdk = 24,
                isSystem = false,
                isDebuggable = false,
                isEnabled = true,
                riskScore = 85,
                riskLevel = "HIGH",
                suspicious = true,
                riskReasons = "",
                recommendUpload = false,
                recommendation = "",
                vtChecked = false,
                vtResult = "",
                scanDate = "2023-10-27",
                scanTime = "10:00:00",
                scanId = "sample_id",
                isDeleted = true,
                iconData = null
            ),
            onClick = {}
        )
    }
}
