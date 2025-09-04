plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.kotlinter)
    alias(libs.plugins.hilt) // Added Hilt plugin
    kotlin("kapt")           // Added Kapt for Hilt
}

android {
    namespace = "com.example.genscanproject"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.genscanproject"
        minSdk = 24
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
//    kotlinOptions { // no long works
//        jvmTarget = "11"
//    }
    kotlin {
        jvmToolchain(11) // This is the modern way
    }
    buildFeatures {
        compose = true
    }
    kotlinter {
        ignoreFailures = false
        reporters = arrayOf("plain")
    }
}

dependencies {

    //Project Module Dependencies
    implementation(project(":app:feature_auth"))
    implementation(project(":app:feature_admin"))
    implementation(project(":app:feature_scanner"))
    implementation(project(":app:feature_generator"))
    implementation(project(":app:feature_profile"))
    implementation(project(":qrcodecomposelib"))
    implementation(project(":qrcodecomposelibmlkit"))
    //UI and Business logic modules
    implementation(project(":app:common"))
    implementation(project(":app:domain"))
    implementation(project(":app:data"))
    implementation(project(":app:di"))

    //Androidx and Compose
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Hilt Dependencies
    implementation(libs.hilt.android) // Added Hilt runtime
    kapt(libs.hilt.compiler)          // Added Hilt compiler

    //Navigation
    // Jetpack Compose Navigation
    implementation("androidx.navigation:navigation-compose:2.9.3") // Or the latest version
    // ViewModel with Compose
    implementation(libs.androidx.lifecycle.viewmodel.compose) // Or the latest version




    //Firestore - Remember to put the dependency versions or they won't work
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