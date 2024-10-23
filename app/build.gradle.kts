plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose.plugin)
    alias(libs.plugins.hilt.plugin)
    alias(libs.plugins.ksp.plugin)
    alias(libs.plugins.room.plugin)
}

android {
    namespace = "com.rkm.tasky"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.rkm.tasky"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    room {
        schemaDirectory("$projectDir/schemas")
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
    buildFeatures {
        compose = true
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.hilt)
    ksp(libs.androidx.hilt.compiler)
    implementation(libs.androidx.room)
    implementation(libs.androidx.room.kotlin)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room)
    implementation(libs.androidx.datastore)
    testImplementation(libs.junit)
    testImplementation(libs.androidx.room.test.helpers)
    testImplementation(libs.robolectric)
    testImplementation(libs.turbine)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mock.web.server)
    testImplementation(libs.mockito)
    testImplementation(libs.mockito.kotlin)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}
