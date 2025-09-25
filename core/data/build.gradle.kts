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
                implementation(libs.bundles.ktor.common)
                implementation(libs.touchlab.kermit)
            }
        }


        androidMain {
            dependencies {
                implementation(libs.ktor.client.okhttp)
            }
        }

        iosMain {
            dependencies {
                implementation(libs.ktor.client.darwin)
            }
        }
    }

}