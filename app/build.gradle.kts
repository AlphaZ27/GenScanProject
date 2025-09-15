plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.gms.google.services) //Google Services plugins
    alias(libs.plugins.kotlinter)
    alias(libs.plugins.hilt) // Added Hilt plugin
    kotlin("kapt")           // Added Kapt for Hilt
}

android {
    namespace = "com.example.genscanproject"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.genscanproject"
        minSdk = 25
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
//    kotlinOptions { // no long works
//        jvmTarget = "11"
//    }

    kotlin {
        jvmToolchain(17) // This is the modern way
    }

    buildFeatures {
        compose = true
        viewBinding = true
    }

    kotlinter {
        ignoreFailures = false
        reporters = arrayOf("plain")
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    //Project Module Dependencies
    implementation(project(":feature_auth"))
    implementation(project(":feature_admin"))
    implementation(project(":feature_scanner"))
    implementation(project(":feature_generator"))
    implementation(project(":feature_profile"))
    implementation(project(":history"))

    //UI and Business logic modules
    implementation(project(":common"))
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":di"))

    //Androidx and Compose
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.appcompat)

    // Hilt Dependencies
    implementation(libs.hilt.android) // Added Hilt runtime
    kapt(libs.hilt.compiler)          // Added Hilt compiler
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")


    //Navigation
    // Jetpack Compose Navigation
    implementation("androidx.navigation:navigation-compose:2.9.3") // Or the latest version
    // ViewModel with Compose
    implementation(libs.androidx.lifecycle.viewmodel.compose) // Or the latest version

    //Google Play Services ML-kit
    implementation("com.google.android.gms:play-services-mlkit-barcode-scanning:18.3.1")

    // Accompanist (for permissions)
    implementation("com.google.accompanist:accompanist-permissions:0.37.3")

    //Camera
    val cameraxVersion = "1.4.2"
    implementation("androidx.camera:camera-core:${cameraxVersion}")
    implementation("androidx.camera:camera-camera2:${cameraxVersion}")
    implementation("androidx.camera:camera-lifecycle:${cameraxVersion}")
    implementation("androidx.camera:camera-view:${cameraxVersion}")
    implementation("androidx.camera:camera-extensions:${cameraxVersion}")

    //Firestore - Remember to put the dependency versions or they won't work
    //firestore
    implementation(platform("com.google.firebase:firebase-bom:34.2.0"))
    implementation("com.google.firebase:firebase-firestore-ktx") // Changed to direct string
    implementation("com.google.firebase:firebase-auth-ktx")
    // Now declare other Firebase libraries without versions
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    //implementation(libs.firebase.bom)
    //implementation(libs.firebase.analytics)
    //Firebase Common library
    //implementation(libs.firebase.common.ktx)
    //implementation(libs.firebase.firestore.ktx)
    //implementation(libs.firebase.auth.ktx)


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}