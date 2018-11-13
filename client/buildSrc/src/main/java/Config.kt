object Config {


    object Android {
        const val compileSdk = 28
        const val mindSdk = 19
        const val targetSdk = 28
        const val buildTools = "28.0.3"
        const val versionCode = 1
        const val versionName = "1.0"
    }


    object Versions {
        const val androidGradlePlugin = "3.2.1"

        const val gradlePlugin = "3.2.1"
        const val kotlin = "1.3.0"
        const val dagger2 = "2.19"

        const val rxJava = "2.2.3"
        const val rxAndroid = "2.1.0"
        const val googleMaps = "16.0.0"
        const val timber = "4.7.1"

        const val androidx = "1.0.0"
        const val androidxConstraintLayout = "1.1.3"
        const val lifecycleVersion = "2.0.0"

        const val easyPermissions = "2.0.0"


        // networking
        const val gson = "2.8.5"
        const val okHttp3 = "3.11.0"
        const val retrofit2 = "2.4.0"

        // testing
        const val androidxRunnerVersion = "1.1.0"
        const val androidxEspressoVersion = "3.1.0"
    }

    object Libraries {
        const val rxJava = "io.reactivex.rxjava2:rxjava:${Versions.rxJava}"
        const val rxAndroid = "io.reactivex.rxjava2:rxandroid:${Versions.rxAndroid}"

        const val timber = "com.jakewharton.timber:timber:${Versions.timber}"

        const val dagger = "com.google.dagger:dagger:${Versions.dagger2}"
        const val daggerAndroid = "com.google.dagger:dagger-android:${Versions.dagger2}"
        const val daggerSupport = "com.google.dagger:dagger-android-support:${Versions.dagger2}"
        const val daggerCompiler = "com.google.dagger:dagger-compiler:${Versions.dagger2}"

        const val androidxAppcompat = "androidx.appcompat:appcompat:${Versions.androidx}"
        const val androidxConstraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.androidxConstraintLayout}"
        const val lifecycleExtensions = "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycleVersion}"
        const val lifecycleCommonJava8 = "androidx.lifecycle:lifecycle-common-java8:${Versions.lifecycleVersion}"
        const val androidxMaterial = "com.google.android.material:material:${Versions.androidx}"
        const val androidxLegacy = "androidx.legacy:legacy-support-v4:1.0.0"

        const val lifecycleViewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycleVersion}"
        const val lifecycleCommon = "androidx.lifecycle:lifecycle-common:${Versions.lifecycleVersion}"
        const val lifecycleRuntime = "androidx.lifecycle:lifecycle-runtime:${Versions.lifecycleVersion}"
        const val lifecycleExtensions2 = "androidx.lifecycle:extensions:${Versions.lifecycleVersion}"

        const val easyPermissions = "pub.devrel:easypermissions:${Versions.easyPermissions}"


        const val gson = "com.google.code.gson:gson:${Versions.gson}"
        const val okhttp = "com.squareup.okhttp3:okhttp:${Versions.okHttp3}"
        const val okhttpLoggingInterceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.okHttp3}"
        // const val okhttpMockWebserver = "com.squareup.okhttp3:mockwebserver:${Versions.okHttp3}"
        const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit2}"
        const val retrofitRxJava = "com.squareup.retrofit2:adapter-rxjava2:${Versions.retrofit2}"
        const val retrofitConverter = "com.squareup.retrofit2:converter-gson:${Versions.retrofit2}"

        const val googleMaps = "com.google.android.gms:play-services-maps:${Versions.googleMaps}"

    }


    object TestLibraries {

        const val androidxCore            = "androidx.test:core:${Versions.androidx}"
        const val junit           = "androidx.test.ext:junit:${Versions.androidx}"
        const val runner          = "androidx.test:runner:${Versions.androidxRunnerVersion}"
        const val rules           = "androidx.test:rules:${Versions.androidxRunnerVersion}"
        const val espressoCore    = "androidx.test.espresso:espresso-core:${Versions.androidxEspressoVersion}"
    }




    object BuildPlugins {
        const val androidPlugin = "com.android.tools.build:gradle:${Versions.androidGradlePlugin}"
        const val kotlinPlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    }



}