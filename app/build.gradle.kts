plugins {
    alias(libs.plugins.qello.android.application)
    alias(libs.plugins.qello.android.hilt)
}

android {
    namespace = "com.qello.app"
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":presentation"))
}
