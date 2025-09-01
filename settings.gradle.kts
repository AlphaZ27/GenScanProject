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
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "GenScanProject"
include(":app")
include(":app:core")
include(":app:feature_scanner")
include(":app:feature_generator")
include(":app:history")
include(":app:feature_auth")
include(":app:feature_profile")
include(":app:feature_admin")

include(":qrcodecomposelib")
include(":qrcodecomposelibmlkit")
