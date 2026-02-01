import kikyo.buildlogic.configureCompose

plugins {
    id("com.android.application")
    kotlin("android")

    id("kikyo.code.lint")
}

android {
    configureCompose(this)
}
