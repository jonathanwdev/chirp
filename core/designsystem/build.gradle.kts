plugins {
    alias(libs.plugins.convention.cmp.library)
}

kotlin {

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                implementation(libs.coil.compose)
                implementation(libs.coil.network.ktor)
                implementation(compose.components.resources)
                implementation(projects.core.presentation)
                implementation(compose.components.uiToolingPreview)
            }
        }


        androidMain {
            dependencies {

            }
        }

        iosMain {
            dependencies {

            }
        }
    }

}

compose.resources {
    publicResClass = true
}