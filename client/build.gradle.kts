
buildscript {

    repositories {
        mavenCentral()
        google()
        jcenter()
    }

    dependencies {

        classpath(Config.BuildPlugins.androidPlugin)
        classpath(Config.BuildPlugins.kotlinPlugin)
        classpath("android.arch.navigation:navigation-safe-args-gradle-plugin:1.0.0-alpha07")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        mavenCentral()
        google()
        jcenter()
    }
}




