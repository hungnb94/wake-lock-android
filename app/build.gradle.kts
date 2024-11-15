plugins {
	alias(libs.plugins.android.application)
	alias(libs.plugins.kotlin.android)
}

android {
	namespace = "com.leoh.wakelockexample"
	compileSdk = 35

	defaultConfig {
		applicationId = "com.leoh.wakelockexample"
		minSdk = 24
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
				"proguard-rules.pro",
			)
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_11
		targetCompatibility = JavaVersion.VERSION_11
	}
	kotlinOptions {
		jvmTarget = "11"
	}
	testOptions {
		unitTests {
			isIncludeAndroidResources = true
		}
	}
}

dependencies {
	implementation(libs.androidx.core.ktx)
	implementation(libs.androidx.appcompat)
	implementation(libs.material)
	implementation(libs.androidx.activity)
	implementation(libs.androidx.constraintlayout)
	implementation(platform(libs.okhttp.bom))
	implementation(libs.okhttp)
	implementation(libs.okhttp.logging.interceptor)
	implementation(libs.retrofit)
	implementation(libs.retrofit.converter.gson)
	implementation(libs.xlog)
	testImplementation(libs.junit)
	testImplementation(libs.androidx.junit)
	testImplementation(libs.truth)
	testImplementation(libs.robolectric)
	androidTestImplementation(libs.androidx.junit)
	androidTestImplementation(libs.androidx.espresso.core)
}
