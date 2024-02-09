package utils

import data.model.Language

object Config {
    const val APP_ID = "com.kdroid.compose.desktop"
    const val ENABLE_TRAY_ICON = false
    const val ALLOW_ONLY_ONE_INSTANCE = false
    const val PORT = 50152 //FOR IPC

    val AVAILABLE_LANGUAGES = listOf(
        Language("en", "English"),
        Language("he", "עברית", true),
        Language("fr", "Français"),
    )

    const val DEFAULT_LANGUAGE = "en"

}