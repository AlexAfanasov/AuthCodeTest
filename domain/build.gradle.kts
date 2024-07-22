@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.sovcom.domain"
    compileSdk = libs.versions.compileSdk.get().toInt()
    defaultConfig {
        testInstrumentationRunner = libs.versions.testRunner.get()
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()

    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    // android test libs
    testImplementation(libs.junit)
    testImplementation(libs.androidx.test.mockito)
    testImplementation(libs.androidx.test.mockito.kotlin)
    // hilt annotations
    implementation(libs.hilt.javax.inject)
    // coroutines
    implementation(libs.coroutines.core)
}