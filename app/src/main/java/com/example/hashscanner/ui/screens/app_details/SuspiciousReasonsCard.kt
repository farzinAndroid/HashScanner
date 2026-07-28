package com.example.hashscanner.ui.screens.app_details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.hashscanner.R
import com.example.hashscanner.data.model.db_entities.AppInfo
import com.example.hashscanner.ui.theme.RedColor
import com.example.hashscanner.ui.theme.spacing
import com.example.hashscanner.ui.ui_utils.InfoCard
import com.example.hashscanner.utils.Constants

@Composable
fun SuspiciousReasonsCard(
    appInfo: AppInfo,
) {

    InfoCard {
        Text(
            text = stringResource(R.string.details_section_title_suspicion_reasons),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.dp16))

        Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.dp12)) {
            val reasons =
                appInfo.riskReasons.split(",").map { it.trim() }.filter { it.isNotEmpty() }

            reasons.forEach { reason ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.dp10)
                ) {
                    Box(
                        modifier = Modifier
                            .size(MaterialTheme.spacing.dp8)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.RedColor)
                    )
                    Text(
                        text = translateRiskReason(reason),
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                    )
                }
            }

        }
    }
}

@Composable
fun translateRiskReason(reason: String): String {
    return when (reason) {
        Constants.RISK_REASON_DEBUGGABLE -> stringResource(R.string.risk_reason_debuggable)
        Constants.RISK_REASON_UNKNOWN_INSTALLER -> stringResource(R.string.risk_reason_unknown_installer)
        Constants.RISK_REASON_OVERLAY -> stringResource(R.string.risk_reason_overlay)
        Constants.RISK_REASON_INSTALL_PACKAGES -> stringResource(R.string.risk_reason_install_packages)
        Constants.RISK_REASON_ACCESSIBILITY -> stringResource(R.string.risk_reason_accessibility)
        Constants.RISK_REASON_MICROPHONE -> stringResource(R.string.risk_reason_microphone)
        Constants.RISK_REASON_CAMERA -> stringResource(R.string.risk_reason_camera)
        Constants.RISK_REASON_READ_SMS -> stringResource(R.string.risk_reason_read_sms)
        Constants.RISK_REASON_SEND_SMS -> stringResource(R.string.risk_reason_send_sms)
        Constants.RISK_REASON_AUTO_START -> stringResource(R.string.risk_reason_auto_start)
        else -> reason
    }
}
