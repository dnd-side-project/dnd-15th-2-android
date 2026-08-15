plugins {
    alias(libs.plugins.qello.android.library)
    alias(libs.plugins.qello.android.compose)
    alias(libs.plugins.qello.android.coil)
    alias(libs.plugins.qello.android.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.qello.presentation"
}

dependencies {
    implementation(project(":domain"))

    implementation(libs.kotlinx.serialization.json)
}
