package com.example.hashscanner.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)



val ColorScheme.BackgroundColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xff121212) else Color(0xffFFFFFF)



val ColorScheme.TopBarColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xff191029) else Color(0xff3D5AFE)




val ColorScheme.AccentPurpleColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xff9A66FF) else Color(0xff3D5AFE)



val ColorScheme.CardBackgroundColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xff242424) else Color(0xffFFFFFF)



val ColorScheme.PrimaryTextColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xffFFFFFF) else Color(0xff000000)


val ColorScheme.SubtleTextColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xffE0E0E0) else Color(0xff757575)


val ColorScheme.SuccessGreenColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xff66BB6A) else Color(0xff4CAF50)


val ColorScheme.MediumRiskYellowColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xffFFEB3B) else Color(0xffFFEB3B)


val ColorScheme.HighRiskRedColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xffF44336) else Color(0xffF44336)


































