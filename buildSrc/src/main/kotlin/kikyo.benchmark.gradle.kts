import kikyo.buildlogic.configureAndroid
import kikyo.buildlogic.configureTest

plugins {
    id("com.android.test")
    kotlin("android")

    id("kikyo.code.lint")
}

android {
    configureAndroid(this)
    configureTest()
}
