package com.example.hashscanner.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.hashscanner.R
import com.example.hashscanner.ui.theme.AccentPurpleColor
import com.example.hashscanner.ui.theme.BackgroundColor
import com.example.hashscanner.ui.theme.BlackWhiteColor
import com.example.hashscanner.ui.theme.HashScannerTheme

@Composable
fun PermissionDialog(
    icon: ImageVector,
    title: String,
    description: String,
    onGrant: () -> Unit,
    onDismiss: () -> Unit,
    grantButtonText: String = stringResource(R.string.button_grant_permission)
) {
    val accentColor = MaterialTheme.colorScheme.AccentPurpleColor
    val bgColor = MaterialTheme.colorScheme.BackgroundColor
    val textColor = MaterialTheme.colorScheme.BlackWhiteColor


    CompositionLocalProvider(LocalLayoutDirection.provides(LayoutDirection.Rtl)) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .wrapContentHeight(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = bgColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Icon Header
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .background(accentColor.copy(alpha = 0.1f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            modifier = Modifier.size(40.dp),
                            tint = accentColor
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Title
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Description
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = textColor.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center,
                        lineHeight = MaterialTheme.typography.bodyLarge.lineHeight * 1.2
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Action Buttons
                    Button(
                        onClick = onGrant,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = accentColor)
                    ) {
                        Text(
                            text = grantButtonText,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.button_later),
                            style = MaterialTheme.typography.bodyMedium,
                            color = textColor.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }

    }

}

@Preview(showBackground = true)
@Composable
fun BatteryPermissionPreview() {
    HashScannerTheme {
        PermissionDialog(
            icon = Icons.Default.Warning,
            title = "Battery Optimization",
            description = "To ensure background scanning works correctly, please disable battery optimization for this app.",
            onGrant = {},
            onDismiss = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationPermissionPreview() {
    HashScannerTheme {
        PermissionDialog(
            icon = Icons.Default.Notifications,
            title = "Notifications",
            description = "Stay informed about your scan results and security alerts by enabling notifications.",
            onGrant = {},
            onDismiss = {}
        )
    }
}
