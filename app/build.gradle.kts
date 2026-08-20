plugins {
    alias(libs.plugins.qello.android.application)
    alias(libs.plugins.qello.android.hilt)
    alias(libs.plugins.secrets.gradle.plugin)
}

android {
    namespace = "com.qello.app"

    buildFeatures {
        buildConfig = true
    }
}

secrets {
    propertiesFileName = "local.properties"
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":presentation"))

    implementation(libs.bundles.mapbox)
}
