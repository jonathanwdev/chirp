import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.convention.cmp.application)
    alias(libs.plugins.compose.hot.reload)
    alias(libs.plugins.google.services)
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.koin.android)
            implementation(libs.core.splashscreen)
        }
        commonMain.dependencies {
            implementation(projects.core.data)
            implementation(projects.core.domain)
            implementation(projects.core.designsystem)
            implementation(projects.core.presentation)

            implementation(projects.feature.auth.domain)
            implementation(projects.feature.auth.presentation)

            implementation(projects.feature.chat.domain)
            implementation(projects.feature.chat.presentation)
            implementation(projects.feature.chat.data)
            implementation(projects.feature.chat.database)
            implementation(libs.bundles.koin.common)

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.jetbrains.compose.viewmodel)
            implementation(libs.jetbrains.lifecycle.compose)
            implementation(libs.jetbrains.compose.runtime)
            implementation(libs.jetbrains.compose.navigation)
        }

        desktopMain.dependencies {
            implementation(projects.core.presentation)
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.kotlin.stdlib)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.jsystemthemedetector)


        }

    }
}

compose.desktop {
    application {
        mainClass = "com.plcoding.chirp.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "chirp"
            packageVersion = "1.0.0"

            modules(
                "java.desktop",
                "java.logging",
                "java.prefs",
                "java.sql",
                "jdk.unsupported"
            )

            buildTypes.release.proguard {
                isEnabled.set(true)
                optimize.set(true)
                obfuscate.set(true)
                configurationFiles.from(project.file("compose-desktop.pro"))
            }
        }

        jvmArgs += listOf(
            "-Xmx2g",
            "--add-opens=java.base/sun.misc=ALL-UNNAMED",
            "--add-opens=java.base/java.nio=ALL-UNNAMED",
            "--add-opens=java.desktop/sun.awt=ALL-UNNAMED",
            "--add-opens=java.desktop/sun.awt.wl=ALL-UNNAMED",
            "--add-opens=java.desktop/sun.awt.X11=ALL-UNNAMED"
        )

    }
}

