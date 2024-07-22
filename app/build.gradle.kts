import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.archivesName

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    kotlin("kapt")
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android.gradle.plugin)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.safe.args)
    alias(libs.plugins.kotlin.parcelize)
}
apply(plugin = "org.jetbrains.kotlin.android")

android {
    namespace = libs.versions.applicationId.get()
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = libs.versions.applicationId.get()
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()

        val major = libs.versions.major.get().toInt()
        val minor = libs.versions.minor.get().toInt()
        val buildNumber = libs.versions.buildNumber.get().toInt()
        versionName = "$major.$minor($buildNumber)"

        archivesName.set("${rootProject.name}-$versionName")

        testInstrumentationRunner = libs.versions.testRunner.get()

        buildConfigField(
            "String",
            "BASE_URL",
            "\"https://testhost.test/v1/\""
        )
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
        dataBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

kapt {
    correctErrorTypes = true

    javacOptions {
        // Increase the max count of errors from annotation processors.
        // Default is 100.
        option("-Xmaxerrs", 500)
    }
}

dependencies {
    implementation(project(path = ":domain"))
    implementation(project(path = ":data"))

    // test libs
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.junit)
    testImplementation(libs.androidx.test.mockito)
    testImplementation(libs.androidx.test.mockito.kotlin)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.androidx.core.testing)

    // android libs
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // serialization
    implementation(libs.kotlin.serilization.json)

    // hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    // room
    implementation(libs.room.runtime)

    // retrofit
    implementation(libs.retrofit)

    // coroutines
    implementation(libs.coroutines.android)

    // model watcher
    implementation(libs.mvi.core.diff)

    // firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)

    // sms retriever
    implementation(libs.play.services.auth)
    implementation (libs.play.services.auth.api.phone)
}