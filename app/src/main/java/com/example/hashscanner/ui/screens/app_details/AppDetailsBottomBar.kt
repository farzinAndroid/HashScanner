package com.example.hashscanner.ui.screens.app_details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.hashscanner.R
import com.example.hashscanner.ui.theme.RedColor
import com.example.hashscanner.ui.theme.spacing

@Composable
fun AppDetailsBottomBar(
    isSystem: Boolean = false,
    onUploadApkClicked:()-> Unit,
    onDeleteClicked:()-> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.dp16),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.dp12)
        ) {
            OutlinedButton(
                onClick = onDeleteClicked,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(MaterialTheme.spacing.dp16),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.RedColor
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.RedColor)
            ) {
                Icon(
                    if (isSystem) Icons.Default.Info else Icons.Default.Delete,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(MaterialTheme.spacing.dp8))
                Text(
                    text = stringResource(
                        if (isSystem) R.string.button_app_info else R.string.button_uninstall_app
                    ),
                    fontWeight = FontWeight.Bold
                )
            }

            Button(
                onClick = onUploadApkClicked,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(MaterialTheme.spacing.dp16),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.RedColor,
                    contentColor = Color.White
                )
            ) {
                Icon(Icons.Default.Share, contentDescription = null)
                Spacer(modifier = Modifier.width(MaterialTheme.spacing.dp8))
                Text(
                    text = stringResource(R.string.button_send_apk_file),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
