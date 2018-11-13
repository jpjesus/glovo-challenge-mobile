import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.config.KotlinCompilerVersion



plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("androidx.navigation.safeargs")
}

android {
    compileOptions {
        setSourceCompatibility(JavaVersion.VERSION_1_8)
        setTargetCompatibility(JavaVersion.VERSION_1_8)
    }

    compileSdkVersion(Config.Android.compileSdk)
    defaultConfig {
        applicationId = "com.adamszewera.glovochallenge"
        minSdkVersion(Config.Android.mindSdk)
        targetSdkVersion(Config.Android.targetSdk)
        versionCode = Config.Android.versionCode
        versionName = Config.Android.versionName
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }


    signingConfigs {
        getByName("debug") {
            storeFile = file("keystore/debug.keystore")
            storePassword = "androiddebug"
            keyAlias = "androiddebugkey"
            keyPassword = "androiddebug"
        }
//        create("release") {
//
//        }
    }


    flavorDimensions("env")
    productFlavors {
        create("mock") {
            dimension = "env"
        }
        create("devel") {
            dimension = "env"

        }
        create("prod") {
            dimension = "env"

        }

        productFlavors.all {
            resValue("string", "google_maps_key", "AIzaSyAzfC99c7qvnTTcCjRpcBoIoWBUT80L29w")
        }
    }

    dataBinding {
        setEnabled(true)
    }


}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(kotlin("stdlib-jdk8", KotlinCompilerVersion.VERSION))

    implementation(Config.Libraries.timber)

    implementation(Config.Libraries.androidxAppcompat)
    implementation(Config.Libraries.androidxConstraintLayout)
    implementation(Config.Libraries.androidxMaterial)
    implementation(Config.Libraries.lifecycleExtensions)
    implementation(Config.Libraries.lifecycleCommonJava8)
    implementation(Config.Libraries.androidxLegacy)
    implementation(Config.Libraries.easyPermissions)



    implementation(Config.Libraries.dagger)
    implementation(Config.Libraries.daggerAndroid)
    kapt(Config.Libraries.daggerCompiler)


    implementation(Config.Libraries.rxJava)
    implementation(Config.Libraries.rxAndroid)
    implementation(Config.Libraries.googleMapsUtils)
    implementation(Config.Libraries.googleMaps) {
        exclude(group = "com.android.support")
    }

    // networking
    implementation(Config.Libraries.gson)
    implementation(Config.Libraries.okhttp)
    implementation(Config.Libraries.okhttpLoggingInterceptor)
    implementation(Config.Libraries.retrofit)
    implementation(Config.Libraries.retrofitRxJava)
    implementation(Config.Libraries.retrofitConverter)
    // const val okhttpMockWebserver = "com.squareup.okhttp3:mockwebserver:${Versions.okHttp3}"


    // testing
//    androidTestImplementation(Config.TestLibraries.androidxCore)
//    androidTestImplementation(Config.TestLibraries.junit)
//    androidTestImplementation(Config.TestLibraries.runner)
//    androidTestImplementation(Config.TestLibraries.rules)
//    androidTestImplementation(Config.TestLibraries.espressoCore)

    testImplementation("junit:junit:4.12")
    androidTestImplementation("com.android.support.test:runner:1.0.2")
    androidTestImplementation("com.android.support.test.espresso:espresso-core:3.0.2")
}





