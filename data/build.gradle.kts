plugins {
    alias(libs.plugins.qello.android.library)
    alias(libs.plugins.qello.android.hilt)
    alias(libs.plugins.qello.android.network)
    alias(libs.plugins.qello.android.dataStore)
    alias(libs.plugins.qello.android.room)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.qello.data"

}

dependencies {
    implementation(project(":domain"))

    implementation(libs.kotlinx.coroutines.play.services)
    implementation(libs.kotlinx.serialization.json)
}