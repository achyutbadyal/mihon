import kikyo.buildlogic.AndroidConfig
import kikyo.buildlogic.configureAndroid
import kikyo.buildlogic.configureTest

plugins {
    id("com.android.application")
    kotlin("android")

    id("kikyo.code.lint")
}

android {
    defaultConfig {
        targetSdk = AndroidConfig.TARGET_SDK
    }
    configureAndroid(this)
    configureTest()
}
