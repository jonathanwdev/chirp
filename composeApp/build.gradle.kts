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

            implementation(compose.desktop.linux_x64)
            implementation(compose.desktop.linux_arm64)
            implementation(compose.desktop.macos_x64)
            implementation(compose.desktop.macos_arm64)
            implementation(compose.desktop.windows_x64)
            implementation(compose.desktop.windows_arm64)
        }

    }
}

compose.desktop {
    application {
        mainClass = "com.plcoding.chirp.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb, TargetFormat.Exe)
            packageName = "com.plcoding.chirp"
            packageVersion = "1.0.0"
        }
    }
}

