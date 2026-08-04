plugins {
    `kotlin-dsl`
}

group = "com.qello.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

kotlin {
    jvmToolchain(21)
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.hilt.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "qello.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = "qello.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidCompose") {
            id = "qello.android.compose"
            implementationClass = "AndroidComposeConventionPlugin"
        }
        register("androidCoil") {
            id = "qello.android.coil"
            implementationClass = "AndroidCoilConventionPlugin"
        }
        register("androidHilt") {
            id = "qello.android.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }
        register("androidNetwork") {
            id = "qello.android.network"
            implementationClass = "AndroidNetworkConventionPlugin"
        }
        register("androidDataStore") {
            id = "qello.android.dataStore"
            implementationClass = "AndroidDataStoreConventionPlugin"
        }
        register("androidRoom") {
            id = "qello.android.room"
            implementationClass = "AndroidRoomConventionPlugin"
        }
        register("jvmLibrary") {
            id = "qello.jvm.library"
            implementationClass = "JvmLibraryConventionPlugin"
        }
    }
}