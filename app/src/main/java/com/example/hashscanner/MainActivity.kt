package com.example.hashscanner

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.core.content.ContextCompat
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.example.hashscanner.ui.navigation.NavGraph
import com.example.hashscanner.ui.navigation.Screens
import com.example.hashscanner.ui.theme.BackgroundColor
import com.example.hashscanner.ui.theme.HashScannerTheme
import com.example.hashscanner.ui.ui_utils.ChangeStatusBarAndNavigationBarColor
import com.example.hashscanner.utils.Constants
import com.example.hashscanner.viewmodel.AppDatabaseViewModel
import com.example.hashscanner.viewmodel.AppViewModel
import com.example.hashscanner.viewmodel.ScannerViewModel
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

                ChangeStatusBarAndNavigationBarColor(
                    context = this,
                    isDarkMode = isSystemInDarkTheme()
                )

                // --- Permission Handling ---
                val requestPermissionLauncher = rememberLauncherForActivityResult(
                    ActivityResultContracts.RequestPermission()
                ) { isGranted: Boolean ->

                }

                LaunchedEffect(Unit) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        val permissionCheck = ContextCompat.checkSelfPermission(
                            this@MainActivity,
                            Manifest.permission.POST_NOTIFICATIONS
                        )
                        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    }
                }

                var initialScanId by remember { 
                    mutableStateOf(intent?.getStringExtra(Constants.EXTRA_SCAN_ID))
                }

                CompositionLocalProvider(LocalLayoutDirection.provides(LayoutDirection.Rtl)) {
                    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
                    Scaffold(
                        modifier = Modifier
                            .fillMaxSize()
                            .navigationBarsPadding()
                            .statusBarsPadding(),
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
                                onRetry = {
                                    appViewModel.retry()
                                }
                            )

                            // Handle notification click navigation
                            LaunchedEffect(initialScanId) {
                                initialScanId?.let { scanId ->
                                    navController.navigate(Screens.Landing)
                                    initialScanId = null // Clear it so it doesn't re-navigate on config change
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
