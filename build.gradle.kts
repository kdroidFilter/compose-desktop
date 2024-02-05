import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.compose
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization") version libs.versions.kotlin.get()
    id("org.jetbrains.compose")
    id("dev.hydraulic.conveyor") version "1.6"
    id("app.cash.sqldelight") version libs.versions.sqldelight.get()
}

group = "io.github.kdroidFilter.compose-desktop"
version = "0.1.9.3"

repositories {
    mavenCentral()
    google()
    maven { url = uri("https://jitpack.io") }
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
}

kotlin {
    jvm {
        withJava()
    }
    jvmToolchain(17)

    sourceSets {
        val jvmMain: KotlinSourceSet by getting {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
                implementation(compose.desktop.currentOs)
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                @OptIn(ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)

                // PreCompose Libraries
                api(libs.precompose)
                api(libs.precompose.viewmodel)

                // Utility Libraries
                implementation(libs.notify)
                implementation(libs.jsystemThemeDetector)
                implementation(libs.logbackClassic)

                // Icons and UI Enhancements
                implementation(libs.compose.material.icons.extended)
                implementation(libs.fontAwesome)
                implementation(libs.richEditorCompose)
                implementation(libs.multiplatformSettings)

                // Ktor for Networking
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.cio)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.gson)

                // Miscellaneous Libraries
                implementation(libs.jsoup)
                implementation(libs.materialKolor)
                implementation(libs.mpfilepicker)
        //        implementation(libs.pdfReader)

                //Database Librairies
                implementation(libs.sqldelight.driver)
                implementation(libs.sqlite.jdbc)

                //App Indicator (because the Tray function of compose is very very horrible)
                implementation("net.java.dev.jna:jna:5.14.0")
                implementation("com.dorkbox:SystemTray:4.4")

            }
        }
    }
}

sqldelight {
    databases {
        create("Database") {
            packageName.set("notes")
            srcDirs.setFrom("src/jvmMain/sqldelight")
            schemaOutputDirectory.set(file("src/jvmMain/sqldelight/queries"))

        }
        linkSqlite.set(true)
    }
}

dependencies {
    // Use the configurations created by the Conveyor plugin to tell Gradle/Conveyor where to find the artifacts for each platform.
    linuxAmd64(compose.desktop.linux_x64)
    macAmd64(compose.desktop.macos_x64)
    macAarch64(compose.desktop.macos_arm64)
    windowsAmd64(compose.desktop.windows_x64)
}

compose.desktop {
    application {
        mainClass = "MainKt"
        buildTypes.release.proguard {
            configurationFiles.from(project.file("root-config.pro"))
        }
        nativeDistributions{
            appResourcesRootDir.set(project.layout.projectDirectory.dir("resources"))
        }
    }
}

// region Work around temporary Compose bugs.
configurations.all {
    attributes {
        // https://github.com/JetBrains/compose-jb/issues/1404#issuecomment-1146894731
        attribute(Attribute.of("ui", String::class.java), "awt")
    }
}
// endregion
