package com.example.hashscanner.ui.screens.landing

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.hashscanner.R
import com.example.hashscanner.data.network.ConnectivityObserver
import com.example.hashscanner.ui.theme.AccentPurpleColor
import com.example.hashscanner.ui.theme.BackgroundColor
import com.example.hashscanner.ui.theme.BlackWhiteColor
import com.example.hashscanner.ui.theme.LightGray
import com.example.hashscanner.ui.theme.Typography
import com.example.hashscanner.ui.theme.spacing
import com.example.hashscanner.ui.ui_utils.CreateNewUUID
import com.example.hashscanner.ui.ui_utils.MainPurpleButton
import com.example.hashscanner.utils.Constants
import com.example.hashscanner.viewmodel.ConnectivityViewModel
import com.example.hashscanner.viewmodel.DataStoreViewModel

@Composable
fun LandingPageScreen(
    onButtonClick: () -> Unit,
    dataStoreViewModel: DataStoreViewModel = hiltViewModel(),
    connectivityViewModel: ConnectivityViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val status by connectivityViewModel.connectivityStatus.collectAsStateWithLifecycle()
    val isNetworkAvailable = status == ConnectivityObserver.Status.Available

    CreateNewUUID(dataStoreViewModel) { myUUID ->
        Constants.UUID = myUUID
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.AccentPurpleColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.4f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.shild),
                contentDescription = stringResource(R.string.content_desc_app_logo),
                modifier = Modifier.size(100.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.dp16))

            Text(
                text = stringResource(R.string.app_name),
                style = Typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.dp4))

            Text(
                text = stringResource(R.string.landing_subtitle_security_scanner),
                style = Typography.bodyMedium,
                color = MaterialTheme.colorScheme.LightGray,
                fontWeight = FontWeight.Bold
            )

        }




        Card(
            modifier = Modifier
                .fillMaxSize()
                .weight(0.6f),
            shape = RoundedCornerShape(
                topStart = MaterialTheme.spacing.dp32,
                topEnd = MaterialTheme.spacing.dp32
            ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.BackgroundColor
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        vertical = MaterialTheme.spacing.dp32,
                        horizontal = MaterialTheme.spacing.dp24
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {


                Image(
                    painter = painterResource(R.drawable.shild_cyber),
                    contentDescription = stringResource(R.string.content_desc_security_illustration),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 48.dp)
                        .height(200.dp),
                    contentScale = ContentScale.Fit
                )


                Column {
                    Text(
                        text = stringResource(R.string.landing_instruction_tap_to_start),
                        style = Typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.BlackWhiteColor,
                        modifier = Modifier
                            .fillMaxWidth(),
                        fontWeight = FontWeight.Bold
                    )


                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.dp16))

                    MainPurpleButton(
                        text = if (isNetworkAvailable) {
                            stringResource(R.string.button_start_scan)
                        } else {
                            stringResource(R.string.check_network_connection)
                        },
                        onClick = {
                            if (isNetworkAvailable) {
                                onButtonClick()
                            } else {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.check_network_connection),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    )
                }
            }
        }
    }

}
