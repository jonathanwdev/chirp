import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

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
                implementation(libs.androidx.room.runtime)
                implementation(libs.sqlite.bundled)

            }
        }



        androidMain {
            dependencies {
                implementation(libs.koin.android)
                implementation(libs.androidx.lifecycle.process)
                implementation(project.dependencies.platform(libs.firebase.bom))
                implementation(libs.firebase.messaging)
                implementation(libs.koin.android)
            }
        }



        iosMain {
            dependencies {

            }
        }
    }

    targets.withType<KotlinNativeTarget>() {
        compilations.getByName("main") {
            cinterops {
                create("network") {
                    defFile(file("src/nativeInterop/cinterop/network.def"))
                }
            }
        }
    }

}