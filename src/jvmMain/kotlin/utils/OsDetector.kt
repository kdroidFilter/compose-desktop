package utils

import java.io.File
import java.util.Locale

object OsDetector {
    enum class OS {
        WINDOWS, MAC, LINUX, UNKNOWN
    }

    val detectedOS: OS by lazy {
        val osName = System.getProperty("os.name").lowercase(Locale.getDefault())
        when {
            osName.contains("win") -> OS.WINDOWS
            osName.contains("mac") -> OS.MAC
            osName.contains("nix") || osName.contains("nux") || osName.contains("aix") -> OS.LINUX
            else -> OS.UNKNOWN
        }
    }

    fun isWindows() = detectedOS == OS.WINDOWS

    fun isMac() = detectedOS == OS.MAC

    fun isLinux() = detectedOS == OS.LINUX
}

fun getRessourcePath(isCommon: Boolean = true): String {
    val tempOsPath = if (!isCommon) {
        when {
            OsDetector.isMac() -> "mac"
            OsDetector.isLinux() -> "linux"
            OsDetector.isWindows() -> "windows"
            else -> "common"
        }
    } else {
        "common"
    }
    val resourcesDir = System.getProperty("compose.application.resources.dir")?.let {
        File(it).toString()
    } ?: ((System.getProperty("user.dir")) + "/resources/$tempOsPath")
    return resourcesDir
}