import java.util.Properties
import java.util.Base64

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hiltPlugin)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.example.hashscanner"
    compileSdk = 36

    val localProperties = Properties()
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        localProperties.load(localPropertiesFile.inputStream())
    }

    val reportBaseUrl = localProperties.getProperty("REPORT_BASE_URL")?.removeSurrounding("\"") ?: ""
    val apkBaseUrl = localProperties.getProperty("APK_BASE_URL")?.removeSurrounding("\"") ?: ""
    val encodedReportBaseUrl = Base64.getEncoder().encodeToString(reportBaseUrl.toByteArray())
    val encodedApkBaseUrl = Base64.getEncoder().encodeToString(apkBaseUrl.toByteArray())

    defaultConfig {
        applicationId = "com.example.hashscanner"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        buildConfigField("String", "ENCODED_REPORT_BASE_URL", "\"$encodedReportBaseUrl\"")
        buildConfigField("String", "ENCODED_APK_BASE_URL", "\"$encodedApkBaseUrl\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    buildFeatures{
        viewBinding = true
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //compose
    implementation(libs.bundles.composeBundle)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    //hilt navigation
    implementation(libs.androidx.hilt.navigation.compose)

    //Room database
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)


    //Gson
    implementation(libs.gson)
    //okhttp
    implementation(libs.okhttp)
    //interceptor
    implementation(libs.okhttp3.logging.interceptor)


    // serialization
    implementation(libs.kotlinx.serialization.json)

    //navigation compose
    implementation(libs.androidx.navigation.compose)

    //datastore
    implementation(libs.androidx.datastore.preferences)

    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.commons.io)
    implementation(libs.commons.codec)
}
