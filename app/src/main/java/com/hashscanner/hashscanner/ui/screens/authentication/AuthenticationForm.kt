package com.hashscanner.hashscanner.ui.screens.authentication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hashscanner.hashscanner.R
import com.hashscanner.hashscanner.ui.ui_utils.MainPurpleButton

@Composable
fun AuthenticationForm(
    paddingValues: PaddingValues,
    isLoading: Boolean,
    onCLick: (String) -> Unit,
    textColor: Color
) {
    var textFieldValue by remember { mutableStateOf(TextFieldValue("")) }
    val isComplete = textFieldValue.text.length == 19
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.topbar_title_authentication),
            style = MaterialTheme.typography.titleLarge,
            color = textColor,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )
        
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = textFieldValue,
            enabled = !isLoading,
            onValueChange = { newValue ->
                val oldText = textFieldValue.text
                val newText = newValue.text

                if (newText.any { it.isWhitespace() }) return@OutlinedTextField
                val digitsOnly = newText.filter { it.isLetterOrDigit() }.take(16).uppercase()
                val formatted = digitsOnly.chunked(4).joinToString("-")

                val digitsBeforeCursor = oldText.take(textFieldValue.selection.start).count { it.isLetterOrDigit() }
                var newSelectionIndex = 0
                var digitsFound = 0
                for (i in formatted.indices) {
                    if (digitsFound == digitsBeforeCursor) {
                        newSelectionIndex = i
                        if (newText.length > oldText.length && formatted.getOrNull(i) == '-') newSelectionIndex++
                        break
                    }
                    if (formatted[i].isLetterOrDigit()) digitsFound++
                    newSelectionIndex = i + 1
                }

                val finalSelection = if (newValue.selection.start == newText.length) {
                    TextRange(formatted.length)
                } else {
                    TextRange(newSelectionIndex.coerceIn(0, formatted.length))
                }

                textFieldValue = TextFieldValue(text = formatted, selection = finalSelection)
            },
            label = { Text(stringResource(R.string.label_activation_key)) },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Characters,
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (isComplete && !isLoading) {
                        onCLick(textFieldValue.text)
                        keyboardController?.hide()
                    }
                }
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        MainPurpleButton(
            text = if (isLoading) stringResource(R.string.status_checking) else stringResource(R.string.button_check_key),
            enabled = !isLoading && textFieldValue.text.isNotEmpty(),
            onClick = {
                onCLick(textFieldValue.text)
                keyboardController?.hide()
            }
        )
        
        Spacer(modifier = Modifier.weight(1f))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = textColor.copy(alpha = 0.3f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Secure Activation Flow",
                style = MaterialTheme.typography.labelMedium,
                color = textColor.copy(alpha = 0.3f)
            )
        }
    }
}
