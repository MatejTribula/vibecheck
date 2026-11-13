plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services") apply false
}

if (file("google-services.json").isFile || file("src/debug/google-services.json").isFile || file("src/release/google-services.json").isFile) {
    apply(plugin = "com.google.gms.google-services")
}

val apiBaseUrl = providers.gradleProperty("vibecheckApiBaseUrl").orElse("http://10.0.2.2:3000/")

android {
    namespace = "dk.sdu.vibecheck"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "dk.sdu.vibecheck"
        minSdk = 35
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"
        resValue("string", "api_base_url", apiBaseUrl.get().trimEnd('/') + "/")
    }

    buildTypes {
        release {
            optimization {
                enable = true
                packageScope = setOf("androidx.**", "kotlin.**", "kotlinx.**")
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        resValues = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.kotlinx.coroutines.core)
    debugImplementation(libs.androidx.compose.ui.tooling)
    //Firebase
    implementation(platform("com.google.firebase:firebase-bom:35.1.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-messaging")
    implementation("androidx.work:work-runtime-ktx:2.11.2")
}
