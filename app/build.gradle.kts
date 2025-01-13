plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
    id ("kotlin-parcelize")
}

android {
    namespace = "com.example.wallpaper_dead_reviewed"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.wallpaper_dead_reviewed"
        minSdk = 27
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        compose = true
    }
}

dependencies {
    //Jetpack compose Integration for better UX
    val composeBom = platform("androidx.compose:compose-bom:2024.12.01")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    //compose compiler and forcing to version
    implementation("androidx.compose.compiler:compiler:1.5.15")
    configurations.all {
        resolutionStrategy {
            force("androidx.compose.compiler:compiler:1.5.15")
        }
    }

    // Choose one of the following:
    // Material Design 3
    implementation("androidx.compose.material3:material3:1.1.2")
    implementation("androidx.compose.material3:material3-window-size-class:1.1.2")

    implementation("androidx.compose.material3:material3")
    // or Material Design 2
    implementation("androidx.compose.material:material")
    // or skip Material Design and build directly on top of foundational components
    implementation("androidx.compose.foundation:foundation")
    // or only import the main APIs for the underlying toolkit systems,
    // such as input and measurement/layout
    implementation("androidx.compose.ui:ui")

    // Android Studio Preview support
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")

    // UI Tests
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Optional - Included automatically by material, only add when you need
    // the icons but not the material library (e.g. when using Material3 or a
    // custom design system based on Foundation)
    implementation("androidx.compose.material:material-icons-core")
    // Optional - Add full set of material icons
    implementation("androidx.compose.material:material-icons-extended")
    // Optional - Add window size utils
    implementation("androidx.compose.material3.adaptive:adaptive")

    // Optional - Integration with activities
    implementation("androidx.activity:activity-compose:1.9.2")
    // Optional - Integration with ViewModels
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.5")
    // Optional - Integration with LiveData
    implementation("androidx.compose.runtime:runtime-livedata")
    // Optional - Integration with RxJava
    implementation("androidx.compose.runtime:runtime-rxjava2")


//glide library for the images
    implementation ("com.github.bumptech.glide:annotations:4.16.0")
    implementation("com.github.bumptech.glide:glide:4.16.0") {
        exclude(group = "com.android.support")
        exclude(group = "org.jetbrains.kotlin")
    }

    kapt ("com.github.bumptech.glide:compiler:4.16.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.16.0") // Add this for annotation processing

    // For using ViewModel with Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")

    // For using the Pager
    implementation ("com.google.accompanist:accompanist-pager:0.31.3-beta")
    implementation ("com.google.accompanist:accompanist-pager-indicators:0.31.3-beta")

    // Coil for image loading
    implementation("io.coil-kt:coil-compose:2.3.0")
    implementation ("org.jetbrains.kotlin:kotlin-stdlib:$1.9.25")

    //Lottie animaiton
    implementation("com.airbnb.android:lottie-compose:6.0.0")
    implementation("com.airbnb.android:lottie:6.0.0")

    // hilt dagger
    implementation("com.google.dagger:hilt-android:2.48")
    implementation("androidx.test.ext:junit-ktx:1.1.5")
    implementation("androidx.activity:activity:1.9.1")
    implementation("com.google.firebase:firebase-database:21.0.0")
    kapt("com.google.dagger:hilt-android-compiler:2.48")

    //shimmer-effect [skelton]
    implementation("com.facebook.shimmer:shimmer:0.5.0")


    //navigation
    implementation("androidx.fragment:fragment-ktx:1.5.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.3")

// Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")

// ViewModel
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.5.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")

// Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.7")
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.7")

    //material UI
    implementation("com.google.android.material:material:1.9.0")

    //firebase // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth:23.0.0")
    implementation("com.google.firebase:firebase-storage-ktx:21.0.1")


    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito:mockito-core:3.+")

    // allows to mock even variable is private
    testImplementation("org.mockito:mockito-inline:3.11.2")

    testImplementation("org.hamcrest:hamcrest:2.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}
