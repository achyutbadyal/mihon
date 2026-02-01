import kikyo.buildlogic.configureCompose

plugins {
    id("com.android.library")

    id("kikyo.code.lint")
}

android {
    configureCompose(this)
}
