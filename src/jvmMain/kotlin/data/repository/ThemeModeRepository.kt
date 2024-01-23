package data.repository

import enums.ThemeMode

object ThemeModeRepository {
    fun getAllModes() : List<ThemeMode> = ThemeMode.entries

    fun getCurrentTheme(currentTheme: String): ThemeMode {
        return when (currentTheme) {
            ThemeMode.LIGHT.value -> ThemeMode.LIGHT
            ThemeMode.DARK.value -> ThemeMode.DARK
            else -> ThemeMode.AUTO
        }
    }

    fun useDarKTheme(currentTheme: String): Boolean {
        return when (currentTheme) {
            ThemeMode.LIGHT.value -> ThemeMode.LIGHT.mode
            ThemeMode.DARK.value -> ThemeMode.DARK.mode
            else -> ThemeMode.AUTO.mode
        }
    }

    fun isAutoMode(currentTheme: String): Boolean = currentTheme == ThemeMode.AUTO.value


}