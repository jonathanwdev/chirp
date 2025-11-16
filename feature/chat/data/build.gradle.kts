plugins {
    alias(libs.plugins.convention.kmp.library)
    alias(libs.plugins.convention.build.konfig)
}

kotlin {

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)

                implementation(projects.core.domain)
                implementation(projects.core.data)
                implementation(projects.feature.chat.domain)
                implementation(projects.feature.chat.database)
                implementation(libs.bundles.ktor.common)
                implementation(libs.koin.core)

            }
        }



        androidMain {
            dependencies {
                implementation(libs.koin.android)
            }
        }



        iosMain {
            dependencies {

            }
        }
    }

}