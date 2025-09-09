plugins {
    alias(libs.plugins.android.library)
    //kotlin("jvm")
}

// The domain module has no Android dependencies, so it doesn't need an Android block.

//android {
//    namespace = "com.example.domain"
//    compileSdk = 36
//
//    defaultConfig {
//        minSdk = 24
//
//        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        consumerProguardFiles("consumer-rules.pro")
//    }
//
//    buildTypes {
//        release {
//            isMinifyEnabled = false
//            proguardFiles(
//                getDefaultProguardFile("proguard-android-optimize.txt"),
//                "proguard-rules.pro"
//            )
//        }
//    }
//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_11
//        targetCompatibility = JavaVersion.VERSION_11
//    }
//}
//
//kotlin {
//    jvmToolchain(11) // This is the modern way
//}

dependencies {
    //Must remain completely independent of the all other modules
    // And should have almost no dependencies
    implementation("javax.inject:javax.inject:1") // Added for @Inject for use cases


//    implementation(libs.androidx.core.ktx)
//    implementation(libs.androidx.appcompat)
//    implementation(libs.material)
//    testImplementation(libs.junit)
//    androidTestImplementation(libs.androidx.junit)
//    androidTestImplementation(libs.androidx.espresso.core)
}