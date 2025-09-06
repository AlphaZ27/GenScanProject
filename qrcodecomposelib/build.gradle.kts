plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlinter)
}

android {
    namespace = "com.pedroid.qrcodecomposelib"
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
    buildFeatures {
        compose = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(11) // This is the modern way
    }
}


kotlinter {
    //ignoreFailures = false
    reporters = arrayOf("plain")
}



dependencies {
    //implementation(project(":core"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.camera2)
    implementation(libs.androidx.camera.view)
    implementation(libs.androidx.camera.lifecycle)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.foundation)
    implementation(libs.foundation.layout)
    implementation(libs.ui)
    implementation(libs.ui.tooling)
    implementation(libs.ui.tooling.preview)

    // for QR Code scanning without google play services.
    implementation(libs.zxing)

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
