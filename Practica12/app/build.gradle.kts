plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.practica12"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.practica12"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // Paso 4 del PDF: dependencias de iconos de Material Design
    // (material-icons-extended es la que pide el PDF; material-icons-core va incluida
    // por la BOM de Compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.core)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)

    // Paso 4 del PDF: dependencias para el consumo de la API REST
    // Retrofit: cliente HTTP con tipado fuerte para consumir la PokeAPI
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    // Gson: convertidor JSON <-> objetos Kotlin (se integra con Retrofit)
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    // Coil: carga las imágenes de los Pokémon (artwork oficial) en Compose
    implementation("io.coil-kt:coil-compose:2.5.0")
    // Navigation Compose: navegación entre las 3 pantallas (Tipos -> Lista -> Detalle)
    implementation("androidx.navigation:navigation-compose:2.7.7")
}
