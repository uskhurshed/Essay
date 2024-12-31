plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.googleGmsGoogleServices)
}

android {
    namespace = "com.easyapps.zkplayer"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.easyapps.zkplayer"
        minSdk = 21
        targetSdk = 35
        versionCode = 300000001
        versionName = "31.0"
    }

    signingConfigs {
        create("release") {
            keyAlias = "key"
            keyPassword = "123456"
            storeFile = file("khurshed")
            storePassword = "123456789"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures{
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation (libs.okhttp)
    implementation (libs.lottie)
    implementation(libs.play.services.ads)
    implementation(libs.mobileads)
    implementation(libs.firebase.messaging)
    implementation(libs.glide)
}