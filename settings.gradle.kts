pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "GenScanProject"
//Core Layers
include(":app")
include(":common") //Shared UI components, resources, utilities
include(":data")    //Data layer - repositories, local/remote data sources
include(":domain")  //Domain layer - business logic, entities, use cases
include(":di")      //All Hilt dependencies - clean and centralised graph
//Feature Modules
include(":feature_scanner")
include(":feature_generator")
include(":history")
include(":feature_auth")
include(":feature_profile")
include(":feature_admin")

