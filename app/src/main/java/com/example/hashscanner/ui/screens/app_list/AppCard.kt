package com.example.hashscanner.ui.screens.app_list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hashscanner.R
import com.example.hashscanner.data.model.RiskLevels
import com.example.hashscanner.data.model.db_entities.AppInfo
import com.example.hashscanner.ui.theme.BlackWhiteColor
import com.example.hashscanner.ui.theme.GreenColor
import com.example.hashscanner.ui.theme.HashScannerTheme
import com.example.hashscanner.ui.theme.LightGray
import com.example.hashscanner.ui.theme.RedColor
import com.example.hashscanner.ui.theme.YellowColor
import com.example.hashscanner.ui.theme.spacing
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
            .padding(horizontal = MaterialTheme.spacing.dp20),
        colors = CardDefaults.cardColors(
            containerColor = riskColor.copy(0.3f)
        ),
        onClick = onClick,
        border = BorderStroke(
            width = 1.dp,
            color = riskColor
        )
    ) {
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
            ) {
                Text(
                    text = appInfo.appName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.BlackWhiteColor,
                    modifier = Modifier.width(150.dp),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )

                Text(
                    text = appInfo.packageName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.LightGray,
                    modifier = Modifier.width(100.dp),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )

                Text(
                    text = stringResource(R.string.format_risk_score_value) +
                            DigitHelper.digitByLang(appInfo.riskScore.toString()) +
                            "/${DigitHelper.digitByLang("100")}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.LightGray,
                    modifier = Modifier
                        .width(100.dp)
                        .padding(top = MaterialTheme.spacing.dp8),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                RiskBadge(
                    riskText = riskText,
                    color = riskColor
                )
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
                certificateValidFrom = 0L,
                certificateValidTo = 0L,
                installer = "installer",
                firstInstallTime = 0L,
                lastUpdateTime = 0L,
                targetSdk = 33,
                minSdk = 24,
                isSystem = false,
                isDebuggable = false,
                isEnabled = true,
                riskScore = 0,
                riskLevel = "LOW",
                suspicious = false,
                riskReasons = "",
                recommendUpload = false,
                recommendation = "",
                vtChecked = false,
                vtResult = "",
                scanDate = "2023-10-27",
                scanTime = "10:00:00"
            ),
            onClick = {}
        )
    }
}
