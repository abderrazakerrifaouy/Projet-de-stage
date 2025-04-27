plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id ("kotlin-parcelize")
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.projet_de_stage"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.projet_de_stage"
        minSdk = 24
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.16.0")
    implementation ("androidx.fragment:fragment-ktx:1.8.6")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.16.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation ("com.cloudinary:kotlin-url-gen:1.7.0")
    implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation(platform("com.google.firebase:firebase-bom:33.12.0"))
    implementation ("com.google.firebase:firebase-storage-ktx")
    implementation ("com.google.firebase:firebase-auth:22.3.0")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation ("com.google.firebase:firebase-firestore-ktx:25.1.3")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.auth)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}