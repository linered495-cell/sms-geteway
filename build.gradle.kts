plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.smsgatewaystarter"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.smsgatewaystarter"
        minSdk = 23
        targetSdk = 32
        versionCode = 1
        versionName = "0.1"
    }
}
