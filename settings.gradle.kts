pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
    }

    plugins {
        val kotlinVersion = "1.8.20"
        val composeVersion = "1.4.0"

        kotlin("jvm").version(kotlinVersion)
        kotlin("multiplatform").version(kotlinVersion)

        id("org.jetbrains.compose").version(composeVersion)
    }
}
rootProject.name = "compose-playground"

