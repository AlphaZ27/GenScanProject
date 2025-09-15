// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    //Firestore dependency
    id("com.google.gms.google-services") version "4.4.3" apply false
    alias(libs.plugins.android.library) apply false
    // Hilt dependency
    id("com.google.dagger.hilt.android") version "2.57.1" apply false
    id("org.jetbrains.kotlin.kapt") version "2.2.10" apply false
}
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.2.2") // match your AGP target
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.0") // match Kotlin version
        classpath("com.google.gms:google-services:4.4.3") // google services plugin
    }
}

// This enforces the Java 17 toolchain for ALL modules in the project. The modern version.
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}
