import org.gradle.kotlin.dsl.implementation

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("plugin.serialization") version "2.1.20"
    id ("com.google.dagger.hilt.android")
    id("kotlin-kapt")
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.insanoid.whatsapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.insanoid.whatsapp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.firebase.database)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    // Jetpack Compose Integration
    implementation("androidx.navigation:navigation-compose:2.9.0-alpha09")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")

    //hilt
    implementation ("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-compiler:2.51.1")
    implementation ("androidx.hilt:hilt-navigation-compose:1.2.0")

          // Use latest BOM version
    implementation ("com.google.firebase:firebase-auth-ktx:23.2.0")
    implementation ("androidx.compose.ui:ui:1.7.8")
// Firebase Authentication

    //coil dependency used to import image for profile
    implementation("io.coil-kt:coil-compose:2.7.0")

    implementation ("androidx.compose.material:material-icons-extended:1.7.8")
    implementation ("androidx.compose.ui:ui-text:1.7.8")
    implementation ("androidx.compose.runtime:runtime-livedata:1.7.8")
    implementation ("androidx.compose.ui:ui-tooling-preview:1.7.8")
    implementation ("androidx.compose:compose-bom:2025.04.00")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.8.1")






}