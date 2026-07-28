package com.example.hashscanner.ui.screens.app_details


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hashscanner.R
import com.example.hashscanner.data.model.RiskLevels
import com.example.hashscanner.ui.theme.GreenColor
import com.example.hashscanner.ui.theme.RedColor
import com.example.hashscanner.ui.theme.StrongYellowColor
import com.example.hashscanner.ui.theme.YellowColor
import com.example.hashscanner.ui.theme.spacing
import com.example.hashscanner.utils.Constants

@Composable
fun RiskBadge(riskLevel: String) {
    val (riskColor, riskTextRes) = when (riskLevel) {
        RiskLevels.SAFE.toString(), RiskLevels.LOW.toString() -> 
            MaterialTheme.colorScheme.GreenColor to R.string.badge_risk_level_low
        RiskLevels.MEDIUM.toString() -> 
            MaterialTheme.colorScheme.StrongYellowColor to R.string.badge_risk_level_medium
        RiskLevels.HIGH.toString() -> 
            MaterialTheme.colorScheme.RedColor to R.string.badge_risk_level_high
        RiskLevels.CRITICAL.toString() -> 
            MaterialTheme.colorScheme.RedColor to R.string.badge_risk_level_very_high
        else -> MaterialTheme.colorScheme.GreenColor to R.string.badge_risk_level_none
    }

    Row(
        modifier = Modifier
            .clip(CircleShape)
            .background(riskColor.copy(alpha = 0.1f))
            .border(1.dp, riskColor, CircleShape)
            .padding(horizontal = MaterialTheme.spacing.dp12, vertical = MaterialTheme.spacing.dp4),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.dp6)
    ) {
        Text(text = Constants.SYMBOL_WARNING, color = riskColor, fontSize = 12.sp)
        Text(
            text = stringResource(riskTextRes),
            color = riskColor,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}