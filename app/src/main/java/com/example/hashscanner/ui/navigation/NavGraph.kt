package com.example.hashscanner.ui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun NavGraph(
    navController: NavHostController
) {


    NavHost(
        navController = navController,
        startDestination = Screens.Home
    ) {

        composable<Screens.Home>{
            Text("home")
        }


        composable<Screens.Details>{
            Text("details")
        }

    }


}