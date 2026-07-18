package com.example.hashscanner.ui.ui_utils

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.hashscanner.ui.theme.AccentPurpleColor
import com.example.hashscanner.ui.theme.Typography
import com.example.hashscanner.ui.theme.spacing

@Composable
fun MainPurpleButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(MaterialTheme.spacing.dp16),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.AccentPurpleColor,
            contentColor = Color.White
        )
    ) {
        Text(
            text = text,
            style = Typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}
