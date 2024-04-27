plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.gms.google-services")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.ankitgupta.kchatapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.ankitgupta.kchatapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        dataBinding = true
    }
}

dependencies {

    implementation("androidx.credentials:credentials:1.2.2")
    // optional - needed for credentials support from play services, for devices running
    // Android 13 and below.
    //  implementation("com.google.android.gms:play-services-auth:21.0.0")
    implementation("androidx.credentials:credentials-play-services-auth:1.2.2")
    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.0")
    //noinspection UseTomlInstead
    implementation("com.firebaseui:firebase-ui-auth:7.2.0")
    implementation(platform("com.google.firebase:firebase-bom:32.8.0"))

    // Add the dependency for the Firebase Authentication library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-auth")

    // Also add the dependency for the Google Play services library and specify its version
    implementation("com.google.android.gms:play-services-auth:21.0.0")
// gson converter
    implementation("com.google.code.gson:gson:2.10.1")
    // glide
    implementation("com.github.bumptech.glide:glide:4.16.0")


    // dagger hilt
    implementation("com.google.dagger:hilt-android:2.48")
    implementation(libs.firebase.storage)
//    implementation("com.google.android.gms:play-services-location:21.1.0")
    ksp("com.google.dagger:hilt-android-compiler:2.48")
    ksp("androidx.hilt:hilt-compiler:1.1.0")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")

    // glide
    implementation ("com.github.bumptech.glide:glide:4.16.0")


    // viewmodel
   // implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
//    ksp("androidx.lifecycle:lifecycle-compiler:2.7.0")
   // implementation ("androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha03")

   // implementation("androidx.annotation:annotation:1.7.0")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.database)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}