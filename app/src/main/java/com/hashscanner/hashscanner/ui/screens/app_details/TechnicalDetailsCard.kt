package com.hashscanner.hashscanner.ui.screens.app_details

import android.text.format.Formatter
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hashscanner.hashscanner.R
import com.hashscanner.hashscanner.data.model.db_entities.AppInfo
import com.hashscanner.hashscanner.ui.theme.BlueColor
import com.hashscanner.hashscanner.ui.theme.BoxGrayColor
import com.hashscanner.hashscanner.ui.theme.GreenColor
import com.hashscanner.hashscanner.ui.theme.HashScannerTheme
import com.hashscanner.hashscanner.ui.theme.RedColor
import com.hashscanner.hashscanner.ui.theme.StrongYellowColor
import com.hashscanner.hashscanner.ui.theme.spacing
import com.hashscanner.hashscanner.ui.ui_utils.InfoCard
import com.hashscanner.hashscanner.utils.Constants
import com.hashscanner.hashscanner.utils.DigitHelper

@Composable
fun TechnicalDetailsCard(
    appInfo: AppInfo,
    apiAction: String? = null,
    apiMessage: String? = null
) {
    val context = LocalContext.current
    val formattedSize = Formatter.formatShortFileSize(context, appInfo.apkSize)

    InfoCard(contentPadding = PaddingValues(0.dp)) {
        DetailRow(
            icon = { Icon(Icons.Default.Settings, contentDescription = null) },
            label = stringResource(R.string.details_label_app_version),
            value = DigitHelper.digitByLang(appInfo.versionName)
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
                        Triple(
                            stringResource(R.string.details_value_signature_system),
                            MaterialTheme.colorScheme.GreenColor,
                            Constants.SYMBOL_CHECK
                        )
                    }

                    Constants.TRUSTED_INSTALLERS.contains(appInfo.installer) -> {
                        Triple(
                            stringResource(R.string.details_value_signature_store),
                            MaterialTheme.colorScheme.GreenColor,
                            Constants.SYMBOL_CHECK
                        )
                    }

                    appInfo.certificateIssuer.contains(Constants.CERT_ISSUER_GOOGLE, ignoreCase = true) ||
                            appInfo.certificateIssuer.contains(Constants.CERT_ISSUER_ANDROID, ignoreCase = true) -> {
                        Triple(
                            stringResource(R.string.details_value_signature_valid),
                            MaterialTheme.colorScheme.GreenColor,
                            Constants.SYMBOL_CHECK
                        )
                    }

                    appInfo.certificateSha256.isNotBlank() -> {
                        Triple(
                            stringResource(R.string.details_value_signature_sideloaded),
                            MaterialTheme.colorScheme.BlueColor,
                            Constants.SYMBOL_INFO
                        )
                    }

                    else -> {
                        Triple(
                            stringResource(R.string.details_value_signature_unknown),
                            MaterialTheme.colorScheme.RedColor,
                            Constants.SYMBOL_CROSS
                        )
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
                label = stringResource(R.string.details_label_certificate_issuer),
                value = appInfo.certificateIssuer
            )
        }

        // --- API Recommended Action Section ---
        if (apiAction != null || apiMessage != null) {
            HorizontalDivider(
                Modifier,
                DividerDefaults.Thickness,
                color = MaterialTheme.colorScheme.BoxGrayColor
            )

            val actionLabel = when (apiAction) {
                "DELETE" -> stringResource(R.string.api_action_delete_recommendation)
                "UPLOAD_APK" -> stringResource(R.string.api_action_upload_recommendation)
                else -> stringResource(R.string.api_action_cloud_status)
            }

            val actionColor = when (apiAction) {
                "DELETE" -> MaterialTheme.colorScheme.RedColor
                "UPLOAD_APK" -> MaterialTheme.colorScheme.StrongYellowColor
                else -> MaterialTheme.colorScheme.GreenColor
            }

            val actionIcon = when (apiAction) {
                "DELETE" -> Icons.Default.Warning
                "UPLOAD_APK" -> Icons.Default.Share
                else -> Icons.Default.Notifications
            }


            val displayMsg = apiMessage.takeIf { !it.isNullOrBlank() } ?: when (apiAction) {
                "NONE" -> stringResource(R.string.api_msg_safe)
                "DELETE" -> stringResource(R.string.api_msg_virus)
                "UPLOAD_APK" -> stringResource(R.string.api_msg_suspicious)
                "WAIT" -> stringResource(R.string.api_msg_wait)
                else -> stringResource(R.string.api_msg_error)
            }

            DetailRow(
                icon = { Icon(actionIcon, contentDescription = null, tint = actionColor) },
                label = actionLabel,
                valueComposable = {
                    Text(
                        text = displayMsg,
                        color = actionColor,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.End,
                        lineHeight = 18.sp
                    )
                }
            )
        }
    }
}

private val mockAppInfo = AppInfo(
    appName = "Telegram",
    iconData = null,
    packageName = "org.telegram.messenger",
    scanId = "scan_123",
    versionName = "10.8.1",
    versionCode = 10000,
    apkName = "telegram.apk",
    apkPath = "/data/app/telegram.apk",
    apkSize = 45000000L,
    md5 = "md5_hash",
    sha1 = "sha1_hash",
    sha256 = "sha256_hash",
    certificateSha1 = "cert_sha1",
    certificateSha256 = "cert_sha256",
    certificateIssuer = "Android",
    certificateSubject = "Android",
    certificateSerial = "123456",
    certificateAlgorithm = "SHA256withRSA",
    certificateValidFrom = "2020-01-01",
    certificateValidTo = "2040-01-01",
    installer = "Google Play Store",
    firstInstallTime = 1600000000000L,
    lastUpdateTime = 1700000000000L,
    targetSdk = 34,
    minSdk = 24,
    isSystem = false,
    isDebuggable = false,
    isEnabled = true,
    riskScore = 65,
    riskLevel = "HIGH",
    suspicious = true,
    riskReasons = "Overlay Permission, Can Install APK",
    recommendUpload = true,
    recommendation = "Upload APK",
    vtChecked = false,
    vtResult = "",
    scanDate = "1403/06/12",
    scanTime = "14:30"
)

@Preview(showBackground = true, name = "1. Upload APK Action Preview")
@Composable
fun TechnicalDetailsCardUploadApkPreview() {
    HashScannerTheme {
        TechnicalDetailsCard(
            appInfo = mockAppInfo,
            apiAction = "UPLOAD_APK",
            apiMessage = "این برنامه مشکوک است. لطفاً فایل APK را برای بررسی بیشتر ارسال کنید."
        )
    }
}

@Preview(showBackground = true, name = "2. Safe Verified Preview")
@Composable
fun TechnicalDetailsCardSafePreview() {
    HashScannerTheme {
        TechnicalDetailsCard(
            appInfo = mockAppInfo,
            apiAction = "NONE",
            apiMessage = "این برنامه توسط ادمین بررسی شد و سالم است."
        )
    }
}

