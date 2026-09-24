plugins {
    alias(libs.plugins.qello.android.library)
    alias(libs.plugins.qello.android.hilt)
    alias(libs.plugins.qello.android.network)
    alias(libs.plugins.qello.android.dataStore)
    alias(libs.plugins.qello.android.room)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.secrets.gradle.plugin)
}

android {
    namespace = "com.qello.data"

    buildFeatures {
        buildConfig = true
    }
}

secrets {
    propertiesFileName = "local.properties"
}

dependencies {
    implementation(project(":domain"))

    implementation(libs.kotlinx.coroutines.play.services)
    implementation(libs.kotlinx.serialization.json)
}
