package com.example.hashscanner.ui.screens.risk_level_list

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.hashscanner.R
import com.example.hashscanner.ui.navigation.Screens
import com.example.hashscanner.ui.theme.AccentPurpleColor
import com.example.hashscanner.ui.theme.BackgroundColor
import com.example.hashscanner.ui.theme.GreenColor
import com.example.hashscanner.ui.theme.RedColor
import com.example.hashscanner.ui.theme.StrongYellowColor
import com.example.hashscanner.ui.theme.spacing
import com.example.hashscanner.ui.ui_utils.AppTopBar
import com.example.hashscanner.ui.ui_utils.RiskLevelItem
import com.example.hashscanner.ui.ui_utils.RiskLevelsUI
import com.example.hashscanner.utils.Constants
import com.example.hashscanner.viewmodel.AppDatabaseViewmodel

@Composable
fun RiskLevelListScreen(
    navController: NavController,
    databaseViewmodel: AppDatabaseViewmodel = hiltViewModel()
) {
    val context = LocalContext.current
    val safeCount by databaseViewmodel.safeAppsCount.collectAsStateWithLifecycle()
    val lowCount by databaseViewmodel.lowRiskAppsCount.collectAsStateWithLifecycle()
    val mediumCount by databaseViewmodel.mediumRiskAppsCount.collectAsStateWithLifecycle()
    val highCount by databaseViewmodel.highRiskAppsCount.collectAsStateWithLifecycle()
    val criticalCount by databaseViewmodel.criticalAppsCount.collectAsStateWithLifecycle()

    LaunchedEffect(true) {
        databaseViewmodel.apply {
            countSafeApps()
            countLowRiskApps()
            countMediumRiskApps()
            countHighRiskApps()
            countCriticalApps()
        }
    }

    val riskLevels = listOf(
        RiskLevelItem(
            title = stringResource(R.string.badge_risk_level_very_high),
            count = criticalCount,
            color = MaterialTheme.colorScheme.RedColor,
            icon = Icons.Default.Warning,
            riskLevel = RiskLevelsUI.CRITICAL,
            isGoToRobot = false
        ),
        RiskLevelItem(
            title = stringResource(R.string.badge_risk_level_high),
            count = highCount,
            color = MaterialTheme.colorScheme.RedColor,
            icon = Icons.Default.Warning,
            riskLevel = RiskLevelsUI.HIGH,
            isGoToRobot = false
        ),
        RiskLevelItem(
            title = stringResource(R.string.badge_risk_level_medium),
            count = mediumCount,
            color = MaterialTheme.colorScheme.StrongYellowColor,
            icon = Icons.Default.Notifications,
            riskLevel = RiskLevelsUI.MEDIUM,
            isGoToRobot = false
        ),
        RiskLevelItem(
            title = stringResource(R.string.badge_risk_level_low),
            count = lowCount,
            color = MaterialTheme.colorScheme.GreenColor,
            icon = Icons.Default.Info,
            riskLevel = RiskLevelsUI.LOW,
            isGoToRobot = false
        ),
        RiskLevelItem(
            title = stringResource(R.string.badge_risk_level_none),
            count = safeCount,
            color = MaterialTheme.colorScheme.GreenColor,
            icon = Icons.Default.CheckCircle,
            riskLevel = RiskLevelsUI.SAFE,
            isGoToRobot = false
        ),
        RiskLevelItem(
            title = stringResource(R.string.go_to_bale_robot),
            count = 0,
            color = MaterialTheme.colorScheme.AccentPurpleColor,
            icon = Icons.Default.Info,
            riskLevel = RiskLevelsUI.SAFE,
            isGoToRobot = true
        )
    )

    Scaffold(
        containerColor = MaterialTheme.colorScheme.BackgroundColor,
        topBar = {
            AppTopBar(
                topBarText = stringResource(R.string.topbar_title_suspicious_apps),
                onClick = {
                    navController.popBackStack()
                }
            )
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(MaterialTheme.spacing.dp16),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.dp16),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.dp16)
        ) {
            items(riskLevels) { item ->
                RiskLevelCard(
                    item = item,
                    onClick = { isRobot ->
                        if (isRobot) {
                            try {
                                val intent = Intent(Intent.ACTION_VIEW, Constants.BALE_BOT_URL.toUri())
                                context.startActivity(intent)
                            } catch (_: Exception) {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.error_opening_url),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        } else {
                            navController.navigate(Screens.AppList(item.riskLevel))
                        }
                    }
                )
            }
        }
    }
}
