@Suppress("DSL_SCOPE_VIOLATION") //https://youtrack.jetbrains.com/issue/KTIJ-19369/False-positive-cant-be-called-in-this-context-by-implicit-receiver-with-plugins-in-Gradle-version-catalogs-as-a-TOML-file
plugins {
    alias(libs.plugins.plugin.kotlin) apply false
    alias(libs.plugins.plugin.kotlin.android) apply false
    alias(libs.plugins.plugin.kotlin.kapt) apply false
    alias(libs.plugins.plugin.kotlin.serialization) apply false
    alias(libs.plugins.plugin.android) apply false
    alias(libs.plugins.plugin.android.lib) apply false

    alias(libs.plugins.plugin.google.services) apply false
    alias(libs.plugins.plugin.firebase.crashlytics) apply false

    alias(libs.plugins.plugin.kotlinter) apply false
    alias(libs.plugins.plugin.versions) apply false

    id("nl.littlerobots.version-catalog-update") version "0.4.0"
    alias(libs.plugins.detekt.gradle.plugin) apply true
    alias(libs.plugins.plugin.hilt) apply false
}

apply(from = "buildscripts/githooks.gradle")
apply(from = "buildscripts/setup.gradle")
apply(from = "buildscripts/versionsplugin.gradle")

allprojects {
    version = configuration.AppConfig.versionName
}

subprojects {
    apply(from = "../buildscripts/detekt.gradle")

    val moduleName = name.replace("-", ".")

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            allWarningsAsErrors = false

            freeCompilerArgs = listOf(
                "-Xallow-result-return-type",
                "-opt-in=kotlin.Experimental"
            )

            jvmTarget = "1.8"
        }
    }

    plugins.withType<com.android.build.gradle.LibraryPlugin> {
        configure<com.android.build.gradle.LibraryExtension> {
            namespace = "${configuration.AppConfig.applicationId}.${moduleName}"

            compileSdk = configuration.AppConfig.compileSdk

            defaultConfig {
                minSdk = configuration.AppConfig.minsdk
                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                vectorDrawables {
                    useSupportLibrary = true
                }
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

            if (moduleName.contains("ui")) {
                buildFeatures {
                    compose = true
                }

                composeOptions {
                    kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
                }
            }

            flavorDimensions.add(configuration.AppConfig.Flavors.dimension)

            productFlavors {
                create(configuration.AppConfig.Flavors.Staging.name) {
                    dimension = configuration.AppConfig.Flavors.dimension
                }

                create(configuration.AppConfig.Flavors.Production.name) {
                    dimension = configuration.AppConfig.Flavors.dimension
                }
            }
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

afterEvaluate {
    // We install the hook at the first occasion
    tasks.named("clean") {
        dependsOn(":installGitHooks")
    }
}

tasks {
    /**
     * The detektAll tasks enables parallel usage for detekt so if this project
     * expands to multi module support, detekt can continue to run quickly.
     *
     * https://proandroiddev.com/how-to-use-detekt-in-a-multi-module-android-project-6781937fbef2
     */
    @Suppress("UnusedPrivateMember")
    val detektAll by registering(io.gitlab.arturbosch.detekt.Detekt::class) {
        parallel = true
        setSource(files(projectDir))
        include("**/*.kt")
        include("**/*.kts")
        exclude("**/resources/**")
        exclude("**/build/**")
        config.setFrom(files("$rootDir/config/detekt/detekt.yml"))
        buildUponDefaultConfig = false
    }
}
