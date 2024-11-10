plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.wsdev.trendingmoviesapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.wsdev.trendingmoviesapp"
        minSdk = 21
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.4" // Agrega la versión del compilador de Compose
    }
}

dependencies {
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("androidx.datastore:datastore-preferences:1.0.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("io.coil-kt:coil-compose:2.0.0")
    implementation ("com.google.accompanist:accompanist-flowlayout:<latest_version>")
    implementation ("io.coil-kt:coil-compose:<latest_version>")
    implementation ("com.google.accompanist:accompanist-flowlayout:0.30.1") // Reemplaza con la versión más reciente que encuentres
    implementation("io.coil-kt:coil-compose:2.4.0")



    // Jetpack Compose y Material 3
    implementation("androidx.compose.ui:ui:1.4.0")
    implementation("androidx.compose.material3:material3:1.1.0")
    implementation("androidx.compose.ui:ui-tooling-preview:1.4.0")
    implementation("androidx.activity:activity-compose:1.6.1")
    implementation ("androidx.compose.foundation:foundation:<latest_version>")


    debugImplementation("androidx.compose.ui:ui-tooling:1.4.0")

    // Dependencia para Navegación en Jetpack Compose
    implementation("androidx.navigation:navigation-compose:2.5.3")

    // Carga de imágenes en Compose (opcional)
    implementation("io.coil-kt:coil-compose:2.3.0")

    // Material Icons
    implementation("androidx.compose.material:material-icons-extended:1.4.0")

    // Dependencias estándar de Android
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")

    // Dependencias de prueba
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.4.0")
    debugImplementation("androidx.compose.ui:ui-tooling:1.4.0")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.4.0")
}
