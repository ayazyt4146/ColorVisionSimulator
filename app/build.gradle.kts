plugins {
    id("com.android.application")
}
android {
    namespace = "com.ayazyt4146.colorvisionsimulator"
    compileSdk = 35
    buildFeatures {
        buildConfig = true
    }
    flavorDimensions += "device"
    productFlavors {
        create("phone") {
            dimension = "device"
            buildConfigField("String", "DEVICE_LAYOUT", "\"phone\"")
        }
        create("tablet") {
            dimension = "device"
            applicationIdSuffix = ".tablet"
            buildConfigField("String", "DEVICE_LAYOUT", "\"tablet\"")
        }
    }
    defaultConfig {
        applicationId = "com.ayazyt4146.colorvisionsimulator"
        minSdk = 23
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }
}
