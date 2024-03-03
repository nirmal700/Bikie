buildscript {

    repositories {
        google()
        mavenCentral()
        maven (
            url  = "https://phonepe.mycloudrepo.io/public/repositories/phonepe-intentsdk-android"
        )
        maven (
            url  = "https://maven.java.net/content/groups/public/"
        )
    }
    dependencies {
        classpath("com.google.gms:google-services:4.4.0")
        classpath("com.android.tools.build:gradle:8.3.0-rc01")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.9")
        classpath("com.google.firebase:perf-plugin:1.4.2")
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.20")
        classpath("com.google.firebase:firebase-appdistribution-gradle:4.0.1")
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.3.0-rc01" apply false
}

