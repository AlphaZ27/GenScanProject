pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
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
include(":app:common") //Shared UI components, resources, utilities
include(":app:data")    //Data layer - repositories, local/remote data sources
include(":app:domain")  //Domain layer - business logic, entities, use cases
include(":app:di")      //All Hilt dependencies - clean and centralised graph
//Feature Modules
include(":app:feature_scanner")
include(":app:feature_generator")
include(":app:history")
include(":app:feature_auth")
include(":app:feature_profile")
include(":app:feature_admin")

