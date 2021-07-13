plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-android-extensions")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

kapt {
    javacOptions {
        option("-source", "8")
        option("-target", "8")
    }
}

val kotlinVersion = "1.5.0"

android {

    compileSdkVersion(30)
    buildToolsVersion = "30.0.3"

    defaultConfig {
        applicationId = "com.xlebnick.kitties"
        minSdkVersion(21)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }

    buildFeatures {
        viewBinding = true
    }

    androidExtensions {
        isExperimental = true
    }
    testOptions {
        unitTests.isReturnDefaultValues = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            isUseProguard = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }

        getByName("debug") {
            versionNameSuffix = "-Debug"
            applicationIdSuffix = ".debug"
        }
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_1_8)
        targetCompatibility(JavaVersion.VERSION_1_8)
    }
}

dependencies {
    val daggerVersion = "2.24"

    val androidxAppCompatVersion = "1.2.0"
    val androidxSwipeRefreshLayout = "1.1.0"
    val androidxNavVersion = "2.3.5"
    val androidxCoreVersion = "1.3.2"
    val androidxLifecycleVersion = "2.2.0"
    val androidxFragmentVersion = "1.2.5"
    val androidxActivityVersion = "1.1.0"
    val androidxTransition = "1.3.1"
    val androidxCardViewVersion = "1.0.0"
    val androidxConstraintLayoutVersion = "1.1.3"
    val androidxBrowserVersion = "1.3.0"
    val materialVersion = "1.2.0"

    val glideVersion = "4.11.0"

    val rxAndroidVersion = "2.1.1"
    val rxJavaVersion = "2.2.12"
    val rxKotlinVersion = "2.4.0"

    val cameraxVersion = "1.0.0-rc01"

    val retrofitVersion = "2.9.0"
    val okhttpLoggingVersion = "4.2.0"
    val livedataKtxVersion = "2.3.1"

    val pagingVersion = "3.0.0"


    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$livedataKtxVersion")

    implementation("androidx.paging:paging-runtime:$pagingVersion")

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlinVersion")
    implementation("com.github.bumptech.glide:glide:$glideVersion")

    kapt("com.github.bumptech.glide:compiler:$glideVersion")
    implementation("io.reactivex.rxjava2:rxandroid:$rxAndroidVersion")
    implementation("io.reactivex.rxjava2:rxjava:$rxJavaVersion")
    implementation("io.reactivex.rxjava2:rxkotlin:$rxKotlinVersion")

    // dagger
    kapt("com.google.dagger:dagger-compiler:$daggerVersion")
    kapt("com.google.dagger:dagger-android-processor:$daggerVersion")
    implementation("com.google.dagger:dagger-android:$daggerVersion")
    implementation("com.google.dagger:dagger-android-support:$daggerVersion")

    // retrofit

    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")
    implementation("com.squareup.okhttp3:logging-interceptor:$okhttpLoggingVersion")

    //
    // androidx: https://developer.android.com/jetpack/androidx/versions/stable-channel
    //
    implementation("androidx.appcompat:appcompat:$androidxAppCompatVersion")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:$androidxSwipeRefreshLayout")
    implementation("androidx.cardview:cardview:$androidxCardViewVersion")
    implementation("androidx.constraintlayout:constraintlayout:$androidxConstraintLayoutVersion")
    implementation("androidx.core:core-ktx:$androidxCoreVersion")
    implementation("androidx.activity:activity:$androidxActivityVersion")
    implementation("androidx.fragment:fragment-ktx:$androidxFragmentVersion")
    implementation("androidx.lifecycle:lifecycle-extensions:$androidxLifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$androidxLifecycleVersion")
    implementation("androidx.navigation:navigation-fragment-ktx:$androidxNavVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$androidxNavVersion")
    implementation("androidx.transition:transition:$androidxTransition")
    implementation("androidx.browser:browser:$androidxBrowserVersion")

    implementation("com.google.android.material:material:$materialVersion")


    // CameraX core library using camera2 implementation
    implementation("androidx.camera:camera-camera2:$cameraxVersion")
    // CameraX Lifecycle Library
    implementation("androidx.camera:camera-lifecycle:$cameraxVersion")
    // CameraX View class
    implementation("androidx.camera:camera-view:1.0.0-alpha24")

    //
    // Unit Tests
    //

    val mockkVersion = "1.10.2"
    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("junit:junit:4.13.2")
    testImplementation("com.github.tmurakami:dexopener:2.0.5")
    testImplementation("androidx.arch.core:core-testing:2.1.0")

}