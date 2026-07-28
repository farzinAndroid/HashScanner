package com.example.hashscanner.ui.screens.authentication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.compose.ui.platform.LocalContext
import com.example.hashscanner.R
import com.example.hashscanner.ui.navigation.Screens
import com.example.hashscanner.ui.theme.GreenColor
import com.example.hashscanner.ui.theme.HashScannerTheme
import com.example.hashscanner.ui.theme.RedColor
import com.example.hashscanner.ui.theme.spacing
import com.example.hashscanner.ui.ui_utils.AppTopBar
import com.example.hashscanner.ui.ui_utils.MainPurpleButton
import com.example.hashscanner.ui.ui_utils.SerialKeyVisualTransformation

@Composable
fun AuthenticationScreen(
    navController: NavHostController
) {
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
                onCLick = {
                    navController.navigate(Screens.Landing)
                }
            )
        }
    )
}

@Composable
fun AuthenticationContent(
    paddingValues: PaddingValues,
    onCLick:()-> Unit,
) {
    var keyText by remember { mutableStateOf("") }
    val isComplete = keyText.length == 16
    val isError = keyText.isNotEmpty() && !isComplete

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(MaterialTheme.spacing.dp24),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        OutlinedTextField(
            value = keyText,
            onValueChange = {

                val sanitized = it.filter { char -> char.isLetterOrDigit() }.take(16).uppercase()
                keyText = sanitized
            },
            label = { Text(stringResource(R.string.label_activation_key)) },
            placeholder = { Text(stringResource(R.string.placeholder_activation_key)) },
            visualTransformation = SerialKeyVisualTransformation(),
            isError = isError,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Characters,
                keyboardType = KeyboardType.Ascii
            ),
            trailingIcon = {
                if (keyText.isNotEmpty()) {
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
            text = stringResource(R.string.button_check_key),
            onClick = {
                if (keyText.isNotEmpty() || keyText.isNotBlank()){
                    onCLick()
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
