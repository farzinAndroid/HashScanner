package com.example.hashscanner.ui.screens.authentication

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.hashscanner.R
import com.example.hashscanner.data.model.api.UserAuthentication
import com.example.hashscanner.data.network.NetworkResult
import com.example.hashscanner.ui.navigation.Screens
import com.example.hashscanner.ui.theme.GreenColor
import com.example.hashscanner.ui.theme.HashScannerTheme
import com.example.hashscanner.ui.theme.spacing
import com.example.hashscanner.ui.ui_utils.AppTopBar
import com.example.hashscanner.ui.ui_utils.MainPurpleButton
import com.example.hashscanner.utils.Constants
import com.example.hashscanner.viewmodel.AppViewModel
import com.example.hashscanner.viewmodel.ScannerViewModel

@Composable
fun AuthenticationScreen(
    navController: NavHostController,
    scannerViewModel: ScannerViewModel = hiltViewModel(),
    appViewModel: AppViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val authenticationResponse by scannerViewModel.authenticationResponse.collectAsStateWithLifecycle()

    LaunchedEffect(authenticationResponse) {
        if (authenticationResponse is NetworkResult.Success) {
            val message = authenticationResponse.data?.message ?: ""
            if (checkActivationStatus(message, context)) {
                appViewModel.saveActivationStatus(true)
                navController.navigate(Screens.Landing) {
                    popUpTo(Screens.Authentication) { inclusive = true }
                }
            }
        } else if (authenticationResponse is NetworkResult.Error) {
            Toast.makeText(context, context.getString(R.string.error_server_connection), Toast.LENGTH_LONG).show()
        }
    }


    Scaffold(
        topBar = {
            AppTopBar(
                topBarText = stringResource(R.string.topbar_title_authentication),
                shouldHaveBackIcon = false
            )
        },
        content = { paddingValues ->
            AuthenticationContent(
                paddingValues = paddingValues,
                isLoading = authenticationResponse is NetworkResult.Loading,
                onCLick = { code ->
                   scannerViewModel.authenticate(
                       UserAuthentication(
                           activationCode = code,
                           deviceId = Constants.UUID
                       )
                   )
                }
            )
        }
    )
}

@Composable
fun AuthenticationContent(
    paddingValues: PaddingValues,
    isLoading: Boolean,
    onCLick: (String) -> Unit,
) {
    var textFieldValue by remember { mutableStateOf(TextFieldValue("")) }
    val isComplete = textFieldValue.text.length == 19
    val isError = textFieldValue.text.isNotEmpty() && !isComplete
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(MaterialTheme.spacing.dp24),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        OutlinedTextField(
            value = textFieldValue,
            enabled = !isLoading,
            onValueChange = { newValue ->
                val oldText = textFieldValue.text
                val newText = newValue.text

                // 1. Strictly block spaces and newlines
                if (newText.any { it.isWhitespace() }) return@OutlinedTextField

                // 2. Extract digits only and limit to 16
                val digitsOnly = newText.filter { it.isLetterOrDigit() }.take(16).uppercase()

                // 3. Format with hyphens
                val formatted = digitsOnly.chunked(4).joinToString("-")

                // 4. Calculate new cursor position
                // Logic: Count how many digits are before the old cursor, then find that digit's position in the new formatted string
                val digitsBeforeCursor = oldText.take(textFieldValue.selection.start).count { it.isLetterOrDigit() }
                
                // If we added a character, we might have added a hyphen too
                var newSelectionIndex = 0
                var digitsFound = 0
                for (i in formatted.indices) {
                    if (digitsFound == digitsBeforeCursor) {
                        // If the user typed a new character, we want to move past it
                        if (newText.length > oldText.length && digitsFound < digitsOnly.length) {
                             // Skip the hyphen if we are exactly at its position
                             newSelectionIndex = i
                             if (formatted.getOrNull(i) == '-') newSelectionIndex++
                        } else {
                            newSelectionIndex = i
                        }
                        break
                    }
                    if (formatted[i].isLetterOrDigit()) digitsFound++
                    newSelectionIndex = i + 1
                }

                // Simpler cursor logic for standard typing at the end
                val finalSelection = if (newValue.selection.start == newText.length) {
                    TextRange(formatted.length)
                } else {
                    TextRange(newSelectionIndex.coerceIn(0, formatted.length))
                }

                textFieldValue = TextFieldValue(
                    text = formatted,
                    selection = finalSelection
                )
            },
            label = { Text(stringResource(R.string.label_activation_key)) },
            placeholder = { Text(stringResource(R.string.placeholder_activation_key)) },
            isError = isError,
            singleLine = true,
            maxLines = 1,
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
            ),
            trailingIcon = {
                if (textFieldValue.text.isNotEmpty()) {
                    if (isComplete) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = stringResource(R.string.content_desc_valid),
                            tint = MaterialTheme.colorScheme.GreenColor
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = stringResource(R.string.content_desc_invalid),
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            },
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.dp24))

        MainPurpleButton(
            text = if (isLoading) stringResource(R.string.status_checking) else stringResource(R.string.button_check_key),
            enabled = !isLoading,
            onClick = {
                if (textFieldValue.text.isNotEmpty() && textFieldValue.text.isNotBlank()){
                    onCLick(textFieldValue.text)
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AuthenticationScreenPreview() {
    HashScannerTheme {
        AuthenticationScreen(
            navController = NavHostController(LocalContext.current)
        )
    }
}



fun checkActivationStatus(text: String, context: Context): Boolean {
    return when (text) {
        "Already Activated" -> {
            Toast.makeText(context, context.getString(R.string.toast_activation_code_active), Toast.LENGTH_LONG).show()
            true
        }

        "Activation Code Already Used" -> {
            Toast.makeText(context, context.getString(R.string.toast_activation_code_used_other_device), Toast.LENGTH_LONG).show()
            true
        }

        "Device Activated" -> {
            Toast.makeText(context, context.getString(R.string.toast_device_activated), Toast.LENGTH_LONG).show()
            true
        }

        else -> {
            Toast.makeText(context, context.getString(R.string.toast_invalid_activation_code), Toast.LENGTH_LONG).show()
            false
        }
    }
}
