package com.example.hashscanner.ui.screens.authentication

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.hashscanner.R
import com.example.hashscanner.data.model.api.UserAuthentication
import com.example.hashscanner.data.network.NetworkResult
import com.example.hashscanner.ui.navigation.Screens
import com.example.hashscanner.ui.theme.AccentPurpleColor
import com.example.hashscanner.ui.theme.BackgroundColor
import com.example.hashscanner.ui.theme.BlackWhiteColor
import com.example.hashscanner.ui.theme.HashScannerTheme
import com.example.hashscanner.utils.Constants
import com.example.hashscanner.viewmodel.AppViewModel
import com.example.hashscanner.viewmodel.ScannerViewModel

@Composable
fun AuthenticationScreen(
    navController: NavHostController,
    scannerViewModel: ScannerViewModel,
    appViewModel: AppViewModel
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

    /*LaunchedEffect(authenticationResponse) {
            val message = authenticationResponse.data?.message ?: ""
            appViewModel.saveActivationStatus(true)
            navController.navigate(Screens.Landing) {
                popUpTo(Screens.Authentication) { inclusive = true }
            }//ebb8a1001409e7ad
    }*/


    AuthenticationScreenContent(
        isLoading = authenticationResponse is NetworkResult.Loading,
        onAuthenticate = { code ->
            scannerViewModel.authenticate(
                UserAuthentication(
                    activationCode = code,
                    deviceId = Constants.DEVICE_ID
                )
            )
        }
    )


}

@Composable
fun AuthenticationScreenContent(
    isLoading: Boolean,
    onAuthenticate: (String) -> Unit
) {
    val accentColor = MaterialTheme.colorScheme.AccentPurpleColor
    val bgColor = MaterialTheme.colorScheme.BackgroundColor
    val textColor = MaterialTheme.colorScheme.BlackWhiteColor

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = bgColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.0f to accentColor,
                            0.2f to accentColor,
                            1.0f to bgColor
                        )
                    )
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.4f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.shild),
                    contentDescription = null,
                    modifier = Modifier.size(120.dp),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.auth_welcome_back),
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.auth_subtitle),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 32.dp)
                )
            }

            // Form Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.6f),
                shape = RoundedCornerShape(
                    topStart = 40.dp,
                    topEnd = 40.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = bgColor
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                AuthenticationForm(
                    paddingValues = PaddingValues(32.dp),
                    isLoading = isLoading,
                    onCLick = onAuthenticate,
                    textColor = textColor
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AuthenticationScreenPreview() {
    HashScannerTheme {
        AuthenticationScreenContent(
            isLoading = false,
            onAuthenticate = {}
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
