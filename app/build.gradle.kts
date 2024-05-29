plugins {
    alias(libs.plugins.androidApplication)
}

android {
    namespace = "com.example.parkingapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.parkingapp"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    packagingOptions{
        exclude ("META-INF/NOTICE.md")
        exclude ("META-INF/LICENSE.md")
    }
}

dependencies {
    implementation ("androidx.work:work-runtime-ktx:2.7.1")
    implementation ("com.sun.mail:android-mail:1.6.7")
    implementation ("com.sun.mail:android-activation:1.6.7")
    implementation ("com.squareup.okhttp3:okhttp:4.9.1")
    implementation ("org.json:json:20210307")
    implementation ("com.airbnb.android:lottie:3.4.0")
    implementation ("me.relex:circleindicator:2.1.6")
    implementation ("androidx.viewpager2:viewpager2:1.0.0")
    implementation ("com.google.android.material:material:1.4.0")
    implementation ("com.google.android.material:material:1.3.0-alpha02")
    implementation ("com.google.android.material:material:1.3.0-alpha01")
    implementation("com.google.android.gms:play-services-maps:18.1.0")
    implementation("androidx.core:core-splashscreen:1.0.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.play.services.maps)
    implementation(libs.car.ui.lib)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}