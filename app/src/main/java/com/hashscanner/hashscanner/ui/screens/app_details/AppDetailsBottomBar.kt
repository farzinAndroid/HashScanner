package com.hashscanner.hashscanner.ui.screens.app_details

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hashscanner.hashscanner.R
import com.hashscanner.hashscanner.ui.theme.*
import com.hashscanner.hashscanner.ui.theme.spacing

@Composable
fun AppDetailsBottomBar(
    isSystem: Boolean = false,
    isUploaded: Boolean = false,
    isServerVerified: Boolean = false,
    isLoading: Boolean = false,
    isDeleted: Boolean = false,
    apiAction: String? = null,
    onUploadApkClicked: () -> Unit,
    onDeleteClicked: () -> Unit
) {
    if (isDeleted) return 

    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.dp16),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.dp12),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // UNINSTALL BUTTON
            // We show this if it's a user app OR if the API specifically recommends DELETE
            val showUninstall = !isSystem || apiAction == "DELETE"
            
            if (showUninstall) {
                OutlinedButton(
                    onClick = onDeleteClicked,
                    modifier = Modifier.weight(1f),
                    enabled = !isLoading,
                    shape = RoundedCornerShape(MaterialTheme.spacing.dp16),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.RedColor
                    ),
                    border = BorderStroke(
                        1.dp,
                        if (apiAction == "DELETE") MaterialTheme.colorScheme.RedColor else MaterialTheme.colorScheme.RedColor.copy(alpha = 0.5f)
                    )
                ) {
                    Icon(Icons.Default.Delete, contentDescription = null)
                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.dp8))
                    Text(
                        text = stringResource(R.string.button_uninstall_app),
                        fontWeight = FontWeight.Bold
                    )
                }
            } else if (isSystem) {
                // For system apps with no specific delete recommendation, show Info button
                OutlinedButton(
                    onClick = onDeleteClicked,
                    modifier = Modifier.weight(1f),
                    enabled = !isLoading,
                    shape = RoundedCornerShape(MaterialTheme.spacing.dp16),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.AccentPurpleColor
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.AccentPurpleColor)
                ) {
                    Icon(Icons.Default.Info, contentDescription = null)
                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.dp8))
                    Text(
                        text = stringResource(R.string.button_app_info),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // UPLOAD BUTTON / STATUS
            if (isUploaded) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isServerVerified) Icons.Default.CheckCircle else Icons.Default.Info,
                        contentDescription = null,
                        tint = if (isServerVerified) MaterialTheme.colorScheme.GreenColor else MaterialTheme.colorScheme.AccentPurpleColor
                    )
                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.dp8))
                    Text(
                        text = stringResource(R.string.apk_already_uploaded),
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isServerVerified) MaterialTheme.colorScheme.GreenColor else MaterialTheme.colorScheme.AccentPurpleColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                Button(
                    onClick = onUploadApkClicked,
                    modifier = Modifier.weight(1f),
                    enabled = !isLoading,
                    shape = RoundedCornerShape(MaterialTheme.spacing.dp16),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.AccentPurpleColor,
                        contentColor = Color.White
                    )
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(Icons.Default.Share, contentDescription = null)
                        Spacer(modifier = Modifier.width(MaterialTheme.spacing.dp8))
                        Text(
                            text = stringResource(R.string.button_send_apk_file),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }
        }
    }
}
