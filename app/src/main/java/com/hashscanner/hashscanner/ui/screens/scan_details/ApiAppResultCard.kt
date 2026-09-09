package com.hashscanner.hashscanner.ui.screens.scan_details

import com.hashscanner.hashscanner.R
import android.graphics.Bitmap
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.hashscanner.hashscanner.data.model.api.ApiAppResult
import com.hashscanner.hashscanner.data.model.db_entities.AppInfo
import com.hashscanner.hashscanner.ui.theme.*
import com.hashscanner.hashscanner.utils.IconConverter

@Composable
fun ApiAppResultCard(
    apiAppResult: ApiAppResult,
    matchingDbApp: AppInfo? = null,
    scanId: String,
    onUploadClicked: (app: AppInfo) -> Unit,
    onDeleteClicked: (app: AppInfo) -> Unit,
    onCardClicked:(packageName: String,scanId: String) -> Unit
) {


    val accentColor = when (apiAppResult.result) {
        "VIRUS" -> MaterialTheme.colorScheme.RedColor
        "SUSPICIOUS" -> MaterialTheme.colorScheme.YellowColor
        "SAFE" -> MaterialTheme.colorScheme.GreenColor
        else -> MaterialTheme.colorScheme.YellowColor
    }

    val actions = when (apiAppResult.action) {
        "NONE" -> stringResource(R.string.api_msg_safe)
        "DELETE" -> stringResource(R.string.api_msg_virus)
        "UPLOAD_APK" -> stringResource(R.string.api_msg_suspicious)
        "WAIT" -> stringResource(R.string.api_msg_wait)
        else -> stringResource(R.string.api_msg_error)
    }






    var isApkUploaded by remember {
        mutableStateOf<Boolean>(false)
    }

    var iconData by remember {
        mutableStateOf<Bitmap?>(null)
    }

    LaunchedEffect(matchingDbApp) {
        iconData = matchingDbApp?.iconData?.let {
            IconConverter.byteArrayToBitmap(it)
        }

        isApkUploaded = matchingDbApp?.apkUploaded ?: false
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = accentColor.copy(alpha = 0.1f),
        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.2f)),
        onClick = {
            matchingDbApp?.let {
                onCardClicked(it.packageName,scanId)
            }
        }
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(accentColor.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    if (iconData != null) {
                        Image(
                            bitmap = iconData!!.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                    } else {
                        Image(
                            painter = painterResource(R.drawable.ic_launcher_foreground),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = apiAppResult.appName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.BlackWhiteColor,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = apiAppResult.packageName,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }

                /*Text(
                    text = DigitHelper.digitByLang(apiAppResult.riskScore.toString()),
                    style = MaterialTheme.typography.titleLarge,
                    color = accentColor,
                    fontWeight = FontWeight.ExtraBold
                )*/
            }

            Spacer(modifier = Modifier.height(12.dp))
            
            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))
            Spacer(modifier = Modifier.height(8.dp))


            if (matchingDbApp?.isDeleted == true) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Done,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.GreenColor,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = stringResource(R.string.status_resolved_threat),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.GreenColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else if (apiAppResult.action == "UPLOAD_APK") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = if (!isApkUploaded) Arrangement.SpaceBetween else Arrangement.Start
                ) {
                    if (!isApkUploaded) {
                        Text(
                            text = actions,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.BlackWhiteColor.copy(alpha = 0.8f),
                            lineHeight = 18.sp,
                            modifier = Modifier.weight(1f)
                        )
                    }


                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.dp12))

                    Button(
                        onClick = {
                            matchingDbApp?.let { onUploadClicked(it) }
                        },
                        shape = RoundedCornerShape(MaterialTheme.spacing.dp12),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.StrongYellowColor,
                            contentColor = Color.White
                        ),
                        contentPadding = PaddingValues(horizontal = MaterialTheme.spacing.dp12, vertical = 0.dp),
                        modifier = Modifier.height(36.dp),
                        enabled = !isApkUploaded && matchingDbApp != null
                    ) {
                        Icon(
                            imageVector = if (!isApkUploaded) Icons.Default.Share else Icons.Default.Done,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(MaterialTheme.spacing.dp8))
                        Text(
                            text = if (!isApkUploaded) stringResource(R.string.button_send_apk_file) else stringResource(R.string.apk_already_uploaded),
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold
                        )
                    }

                }

            } else if (apiAppResult.action == "DELETE") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = actions,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.BlackWhiteColor.copy(alpha = 0.8f),
                        lineHeight = 18.sp,
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.dp12))

                    Button(
                        onClick = { matchingDbApp?.let { onDeleteClicked(it) } },
                        shape = RoundedCornerShape(MaterialTheme.spacing.dp12),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.RedColor,
                            contentColor = Color.White
                        ),
                        contentPadding = PaddingValues(horizontal = MaterialTheme.spacing.dp12, vertical = 0.dp),
                        modifier = Modifier.height(36.dp),
                        enabled = matchingDbApp != null
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(MaterialTheme.spacing.dp8))
                        Text(
                            text = stringResource(R.string.button_uninstall_app),
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            } else {
                Text(
                    text = actions,
                    style = MaterialTheme.typography.bodySmall,
                    color = accentColor,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun ApiAppResultCardPreview() {
    HashScannerTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ApiAppResultCard(
                apiAppResult = ApiAppResult(
                    appName = "Virus App",
                    packageName = "com.example.virus",
                    sha256 = "123",
                    riskScore = 95,
                    riskLevel = "CRITICAL",
                    reason = "Malware",
                    result = "VIRUS",
                    action = "DELETE",
                    message = "این برنامه بدافزار است."
                ),
                matchingDbApp = null,
                onUploadClicked = {},
                onDeleteClicked = {},
                onCardClicked = {packageName, scanId -> },
                scanId = ""
            )

            ApiAppResultCard(
                apiAppResult = ApiAppResult(
                    appName = "Suspicious App",
                    packageName = "com.example.suspicious",
                    sha256 = "456",
                    riskScore = 70,
                    riskLevel = "HIGH",
                    reason = "Overlay",
                    result = "SUSPICIOUS",
                    action = "UPLOAD_APK",
                    message = "این برنامه مشکوک است."
                ),
                matchingDbApp = null,
                onUploadClicked = {},
                onDeleteClicked = {},
                onCardClicked = {packageName, scanId -> },
                scanId = ""
            )
        }
    }
}
