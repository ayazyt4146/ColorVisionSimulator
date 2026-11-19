plugins {
    id("com.android.application")
}
android {
    namespace = "com.ayazyt4146.colorvisionsimulator"
    compileSdk = 35
    flavorDimensions += "device"
    productFlavors {
        create("phone") {
            dimension = "device"
            resValue("string", "app_layout", "phone")
        }
        create("tablet") {
            dimension = "device"
            applicationIdSuffix = ".tablet"
            resValue("string", "app_layout", "tablet")
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
