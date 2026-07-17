package com.example.hashscanner.ui.screens.app_details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hashscanner.R
import com.example.hashscanner.data.model.RiskLevels
import com.example.hashscanner.data.model.db_entities.AppInfo
import com.example.hashscanner.ui.theme.GreenColor
import com.example.hashscanner.ui.theme.RedColor
import com.example.hashscanner.ui.theme.YellowColor
import com.example.hashscanner.ui.theme.spacing
import com.example.hashscanner.utils.IconConverter

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
                fontWeight = FontWeight.Bold
            )
            Text(
                text = appInfo.packageName,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.dp8))
            RiskBadge(appInfo.riskLevel)
        }
    }
}


