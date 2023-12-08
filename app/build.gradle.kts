plugins {
    alias(libs.plugins.plugin.android)
    alias(libs.plugins.plugin.kotlin.android)
    alias(libs.plugins.plugin.kotlin.serialization)
    alias(libs.plugins.plugin.kotlin.kapt)
    alias(libs.plugins.plugin.google.services)
    alias(libs.plugins.plugin.firebase.crashlytics)
    id("com.google.dagger.hilt.android")
}

android {
    compileSdk = configuration.AppConfig.compileSdk
    namespace = configuration.AppConfig.applicationId

    defaultConfig {
        applicationId = configuration.AppConfig.applicationId
        minSdk = configuration.AppConfig.minsdk
        targetSdk = configuration.AppConfig.targetSdk
        versionCode = configuration.AppConfig.versionCode
        versionName = configuration.AppConfig.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        create("release") {
            keyAlias =
                findProperty(configuration.properties.KeyStorePropertiesConfig.keyAlias).toString()
            keyPassword =
                findProperty(configuration.properties.KeyStorePropertiesConfig.keyPassword).toString()
            storeFile =
                file(findProperty(configuration.properties.KeyStorePropertiesConfig.keyStoreFile).toString())
            storePassword =
                findProperty(configuration.properties.KeyStorePropertiesConfig.keyStorePassword).toString()
        }
    }

    buildTypes {
        getByName("debug") {
            manifestPlaceholders["backup"] = "true"
            signingConfig = signingConfigs.getByName("release")
        }

        getByName("release") {
            signingConfig = signingConfigs.getByName("release")

            isMinifyEnabled = false
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            manifestPlaceholders["backup"] = "true"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }

    flavorDimensions.add(configuration.AppConfig.Flavors.dimension)

    productFlavors {
        create(configuration.AppConfig.Flavors.Staging.name) {
            applicationIdSuffix = configuration.AppConfig.Flavors.Staging.suffix
            versionCode = configuration.AppConfig.Flavors.Staging.versionCode
            dimension = configuration.AppConfig.Flavors.dimension
        }

        create(configuration.AppConfig.Flavors.Uat.name) {
            applicationIdSuffix = configuration.AppConfig.Flavors.Uat.suffix
            versionCode = configuration.AppConfig.Flavors.Uat.versionCode
            dimension = configuration.AppConfig.Flavors.dimension
        }

        create(configuration.AppConfig.Flavors.Production.name) {
            dimension = configuration.AppConfig.Flavors.dimension
        }
    }
}

dependencies {
    val composeBom = platform(libs.compose.bom)
    implementation(composeBom)
    debugImplementation(composeBom)
    androidTestImplementation(composeBom)

    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.room.runtime)
    implementation(libs.android.material)
    implementation(libs.compose.ui)
    implementation(libs.compose.material)
    implementation(libs.compose.ui.tooling)
    implementation(libs.hilt.android)
    implementation(libs.square.retrofit)
    implementation(libs.appyx.navigation)
    implementation(libs.appyx.interactions)
    implementation(libs.appyx.backstack)
    implementation(libs.appyx.spotlight)
    implementation(libs.koin.compose)
    implementation(libs.koin.android)


    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.compose.ui.test.junit)
    androidTestImplementation(libs.hilt.android.testing)

    debugImplementation(libs.compose.ui.test.manifest)
    debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.square.leakcanary)

    kapt(libs.androidx.room.compiler)
    kapt(libs.hilt.compiler)
    kaptAndroidTest(libs.hilt.android.compiler)

    annotationProcessor(libs.androidx.room.compiler)
}
