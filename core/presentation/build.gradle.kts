plugins {
    alias(libs.plugins.convention.cmp.library)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                implementation(projects.core.domain)
                implementation(compose.components.resources)
                implementation(libs.bundles.koin.common)
                implementation(libs.jetbrains.lifecycle.compose)

                implementation(libs.material3.adaptive)

            }
        }

       mobileMain.dependencies {
           implementation(libs.moko.permissions)
           implementation(libs.moko.permissions.compose)
           implementation(libs.moko.permissions.notifications)
       }

    }

}