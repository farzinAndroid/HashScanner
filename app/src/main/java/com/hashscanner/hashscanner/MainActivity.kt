package com.hashscanner.hashscanner

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.hashscanner.hashscanner.ui.components.PermissionDialog
import com.hashscanner.hashscanner.ui.navigation.NavGraph
import com.hashscanner.hashscanner.ui.navigation.Screens
import com.hashscanner.hashscanner.ui.theme.BackgroundColor
import com.hashscanner.hashscanner.ui.theme.HashScannerTheme
import com.hashscanner.hashscanner.ui.ui_utils.ChangeStatusBarAndNavigationBarColor
import com.hashscanner.hashscanner.ui.ui_utils.PermissionStep
import com.hashscanner.hashscanner.utils.PermissionUtils
import com.hashscanner.hashscanner.utils.PermissionUtils.requestIgnoreBatteryOptimizations
import com.hashscanner.hashscanner.utils.Constants
import com.hashscanner.hashscanner.utils.PermissionUtils.checkNotificationPermission
import com.hashscanner.hashscanner.utils.PermissionUtils.performPermissionCheck
import com.hashscanner.hashscanner.viewmodel.AppDatabaseViewModel
import com.hashscanner.hashscanner.viewmodel.AppViewModel
import com.hashscanner.hashscanner.viewmodel.ScannerViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val appViewModel: AppViewModel by viewModels()
    private val scannerViewModel: ScannerViewModel by viewModels()
    private val appDatabaseViewModel: AppDatabaseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition {
            !appViewModel.isReady.value
        }
        setContent {
            HashScannerTheme {
                val navController = rememberNavController()
                val isReady by appViewModel.isReady.collectAsStateWithLifecycle()
                val startDestination by appViewModel.startDestination.collectAsStateWithLifecycle()
                val isChecking by appViewModel.isChecking.collectAsStateWithLifecycle()

                val context = LocalContext.current
                val lifecycleOwner = LocalLifecycleOwner.current

                ChangeStatusBarAndNavigationBarColor(
                    context = this,
                    isDarkMode = isSystemInDarkTheme()
                )

                // --- Permission Handling State ---
                var currentStep by rememberSaveable { mutableStateOf(PermissionStep.CHECKING) }
                var batteryCheckInterrupted by rememberSaveable { mutableStateOf(false) }

                val requestPermissionLauncher = rememberLauncherForActivityResult(
                    ActivityResultContracts.RequestPermission()
                ) { isGranted: Boolean ->
                    currentStep = PermissionStep.NONE
                }



                LaunchedEffect(Unit) {
                    performPermissionCheck(
                        currentStep = currentStep,
                        context = context,
                        onStepChanged = { newStep ->
                            currentStep = newStep
                        }
                    )
                }

                // Re-check when returning to app (e.g., from settings)
                DisposableEffect(lifecycleOwner) {
                    val observer = LifecycleEventObserver { _, event ->
                        if (event == Lifecycle.Event.ON_RESUME) {
                            if (currentStep == PermissionStep.BATTERY || currentStep == PermissionStep.CHECKING) {
                                performPermissionCheck(
                                    currentStep = currentStep,
                                    context = context,
                                    onStepChanged = { newStep ->
                                        // If we are still stuck on BATTERY after returning, 
                                        // set a flag to allow manual skip
                                        if (newStep == PermissionStep.BATTERY && currentStep == PermissionStep.BATTERY) {
                                            batteryCheckInterrupted = true
                                        }
                                        currentStep = newStep
                                    }
                                )
                            }
                        }
                    }
                    lifecycleOwner.lifecycle.addObserver(observer)
                    onDispose {
                        lifecycleOwner.lifecycle.removeObserver(observer)
                    }
                }

                var initialScanId by remember {
                    mutableStateOf(intent?.getStringExtra(Constants.EXTRA_SCAN_ID))
                }

                CompositionLocalProvider(LocalLayoutDirection.provides(LayoutDirection.Rtl)) {
                    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
                    Scaffold(
                        modifier = Modifier
                            .fillMaxSize(),
                        containerColor = MaterialTheme.colorScheme.BackgroundColor
                    ) { innerPadding ->

                        if (isReady) {
                            NavGraph(
                                navController = navController,
                                startDestination = startDestination,
                                isChecking = isChecking,
                                appViewModel = appViewModel,
                                scannerViewModel = scannerViewModel,
                                appDatabaseViewModel = appDatabaseViewModel,
                                paddingValues = innerPadding,
                                onRetry = {
                                    appViewModel.retry()
                                }
                            )

                            // Handle notification click navigation
                            LaunchedEffect(initialScanId) {
                                initialScanId?.let { scanId ->
                                    navController.navigate(Screens.Landing)
                                    initialScanId = null
                                }
                            }
                        } else {
                            // Loading state to avoid blank screen while isReady is false
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(48.dp)
                                )
                            }
                        }

                        // Dialogs moved inside RTL provider and layered on top of content
                        when (currentStep) {
                            PermissionStep.BATTERY -> {
                                PermissionDialog(
                                    icon = Icons.Default.Warning,
                                    title = stringResource(R.string.permission_battery_title),
                                    description = if (batteryCheckInterrupted && PermissionUtils.isXiaomi())
                                        stringResource(R.string.permission_battery_xiaomi_desc)
                                    else stringResource(R.string.permission_battery_desc),
                                    grantButtonText = stringResource(R.string.button_grant_permission),
                                    onGrant = {
                                        if (batteryCheckInterrupted) {
                                            // Force move to next step on Xiaomi/stuck cases
                                            checkNotificationPermission(context) { needs ->
                                                currentStep =
                                                    if (needs) PermissionStep.NOTIFICATION else PermissionStep.NONE
                                            }
                                        } else {
                                            requestIgnoreBatteryOptimizations(context)
                                        }
                                    },
                                    onDismiss = {
                                        checkNotificationPermission(context) { needs ->
                                            currentStep =
                                                if (needs) PermissionStep.NOTIFICATION else PermissionStep.NONE
                                        }
                                    }
                                )
                            }

                            PermissionStep.NOTIFICATION -> {
                                PermissionDialog(
                                    icon = Icons.Default.Notifications,
                                    title = stringResource(R.string.permission_notification_title),
                                    description = stringResource(R.string.permission_notification_desc),
                                    onGrant = {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                        } else {
                                            currentStep = PermissionStep.NONE
                                        }
                                    },
                                    onDismiss = {
                                        currentStep = PermissionStep.NONE
                                    }
                                )
                            }

                            else -> {}
                        }
                    }
                }
            }
        }
    }
}


