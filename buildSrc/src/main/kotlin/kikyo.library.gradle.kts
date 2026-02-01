import kikyo.buildlogic.configureAndroid
import kikyo.buildlogic.configureTest

plugins {
    id("com.android.library")

    id("kikyo.code.lint")
}

android {
    configureAndroid(this)
    configureTest()
}
