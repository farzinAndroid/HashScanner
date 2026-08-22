package com.example.hashscanner.ui.screens.scan_details

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hashscanner.R
import com.example.hashscanner.ui.theme.AccentPurpleColor
import com.example.hashscanner.ui.theme.BlackWhiteColor
import com.example.hashscanner.ui.theme.HashScannerTheme
import com.example.hashscanner.utils.DigitHelper

@Composable
fun ApiProgressCard(
    message: String,
    total: Int,
    checked: Int,
    pending: Int,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.AccentPurpleColor.copy(alpha = 0.5f),
        tonalElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.report_cloud_analyzing),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.BlackWhiteColor
                )
                
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.AccentPurpleColor
                )
            }

            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color =  MaterialTheme.colorScheme.BlackWhiteColor.copy(0.5f)
            )

            if (total > 0) {
                val progress = checked.toFloat() / total
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.fillMaxWidth().height(8.dp),
                        strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(R.string.report_label_checked_count, DigitHelper.digitByLang(checked.toString())),
                            style = MaterialTheme.typography.labelSmall
                        )
                        Text(
                            text = stringResource(R.string.report_label_remaining_count, DigitHelper.digitByLang(pending.toString())),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            } else {
                // If counts are not available yet (Stage: SCANNING or WAITING_REPORTS)
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth().height(4.dp),
                    strokeCap = androidx.compose.ui.graphics.StrokeCap.Round,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Composable
fun ApiProgressCardLightPreview() {
    HashScannerTheme {
        Surface {
            Box(modifier = Modifier.padding(16.dp)) {
                ApiProgressCard(
                    message = "گزارش اولیه هنوز آماده نیست. در انتظار بررسی ادمین.",
                    total = 17,
                    checked = 15,
                    pending = 2
                )
            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark Mode")
@Composable
fun ApiProgressCardDarkPreview() {
    HashScannerTheme {
        Surface {
            Box(modifier = Modifier.padding(16.dp)) {
                ApiProgressCard(
                    message = "گزارش اولیه هنوز آماده نیست. در انتظار بررسی ادمین.",
                    total = 17,
                    checked = 15,
                    pending = 2
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ApiProgressCardInitialPreview() {
    HashScannerTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            ApiProgressCard(
                message = "در حال دریافت اطلاعات...",
                total = 0,
                checked = 0,
                pending = 0
            )
        }
    }
}
