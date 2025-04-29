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
    implementation (libs.materialdatetimepicker)
    implementation (libs.firebase.database.ktx)
    implementation (libs.glide)
    implementation (libs.androidx.fragment.ktx)
    annotationProcessor (libs.glide.compiler)
    implementation(libs.logging.interceptor)
    implementation(libs.okhttp)
    implementation (libs.kotlin.url.gen)
    implementation (libs.androidx.swiperefreshlayout)
    implementation(platform(libs.firebase.bom))
    implementation (libs.firebase.storage.ktx)
    implementation (libs.firebase.auth.v2230)
    implementation (libs.androidx.lifecycle.viewmodel.ktx)
    implementation (libs.firebase.firestore.ktx)
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