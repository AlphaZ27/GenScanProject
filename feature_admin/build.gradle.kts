plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose) // Kotlin Compose plugin
    alias(libs.plugins.hilt)
    id("kotlin-kapt")
}

android {
    namespace = "com.example.feature_admin"
    compileSdk = 36

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    kotlin {
        jvmToolchain(11) // This is the modern way
    }
    buildFeatures {
        compose = true // Enabled Compose
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3" // Use your project's version
    }
}

dependencies {

    //UI and Business logic modules
    implementation(project(":common"))
    implementation(project(":domain"))
    implementation(project(":di"))

    //Hilt dependency injection
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0") // For hiltViewModel

    // Core Android & Lifecycle
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Jetpack Compose
    implementation(platform(libs.androidx.compose.bom)) // BOM for managing Compose versions
    implementation(libs.androidx.ui) // Kept as libs.androidx.ui, very standard
    implementation("androidx.compose.foundation:foundation") // Changed to direct string
    implementation("androidx.compose.material3:material3")  // Changed to direct string
    implementation("androidx.compose.ui:ui-tooling-preview") // Changed to direct string
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose") // Changed to direct string

    // You might not need these if this module is fully Compose.
    // libs.androidx.appcompat is for View-based compatibility.
    // libs.material is for View-based Material components.
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}