@file:Suppress("UNUSED_EXPRESSION")

import com.android.build.api.dsl.Packaging
import java.util.Properties

plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("com.google.firebase.firebase-perf")
    id("com.google.firebase.appdistribution")
}
val apiKeysPropertiesFile = rootProject.file("local.properties")
val apiKeysProperties = Properties().apply {
    load(apiKeysPropertiesFile.inputStream())
}
android {
    signingConfigs {
        create("release") {
            storeFile = file("/Users/nirmalkumar/BikieKeystore")
            keyAlias = "keyBikie"
            storePassword = "Nirmal@5689"
            keyPassword = "Nirmal@5689"
        }
    }
    namespace = "com.bikie.in"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.bikie.in"
        minSdk = 24
        targetSdk = 34
        versionCode = 4
        versionName = "4.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        android.buildFeatures.buildConfig = true
        buildConfigField("String", "PHONEPE_UAT_SALT", "\"${apiKeysProperties.getProperty("API_KEY_UAT_PHONEPE")}\"")
        buildConfigField("String", "PHONEPE_PROD_SALT", "\"${apiKeysProperties.getProperty("API_KEY_RELEASE_PHONEPE")}\"")
        buildConfigField("String", "MAIL_ID", "\"${apiKeysProperties.getProperty("API_GMAIL_ID")}\"")
        buildConfigField("String", "MAIL_PASSWORD", "\"${apiKeysProperties.getProperty("API_GMAIL_PASSWORD")}\"")
        signingConfig = signingConfigs.getByName("release")
    }


    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            isDebuggable = false

        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    // picks the JavaMail license file
    fun Packaging.() {
        pickFirst("META-INF/LICENSE.txt") // picks the JavaMail license file
    }
}

dependencies {

    implementation("com.airbnb.android:lottie:6.1.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-database:20.3.1")
    implementation("com.google.firebase:firebase-firestore:24.10.3")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    implementation("com.google.firebase:firebase-crashlytics:18.6.2")
    implementation("com.google.firebase:firebase-analytics:21.5.1")
    implementation("com.google.firebase:firebase-perf:20.5.2")
    implementation("com.google.firebase:firebase-auth:22.3.1")
    implementation("com.google.firebase:firebase-config:21.6.3")
    implementation("com.google.firebase:firebase-inappmessaging-display:20.4.0")
    implementation("com.google.firebase:firebase-messaging:23.4.1")
    implementation("com.google.firebase:firebase-functions:20.4.0")
    implementation("androidx.activity:activity:1.8.2")
    testImplementation("junit:junit:4.13.2")
    implementation(platform("com.google.firebase:firebase-bom:32.7.4"))
    implementation("com.google.firebase:firebase-appcheck-playintegrity")
    implementation("com.google.firebase:firebase-appcheck-debug")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("com.github.dhaval2404:imagepicker:2.1")  //Image Picker
    implementation ("org.mindrot:jbcrypt:0.4")
    // Amplify API and Datastore dependencies
    implementation ("com.amplifyframework:aws-api:2.14.5")
    implementation ("com.amplifyframework:aws-datastore:2.14.5")
    implementation ("com.amplifyframework:aws-auth-cognito:2.14.5")
    implementation ("phonepe.intentsdk.android.release:IntentSDK:2.4.3")
    implementation("com.android.volley:volley:1.2.1")

    // Support for Java 8 features
    coreLibraryDesugaring ("com.android.tools:desugar_jdk_libs:2.0.4")
    implementation ("com.sun.mail:android-mail:1.6.2")
    implementation ("com.sun.mail:android-activation:1.6.2")
    implementation ("com.itextpdf:html2pdf:4.0.5")
}