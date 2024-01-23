package utils

import com.jthemedetecor.OsThemeDetector

/*
Use like this :
    var isDarkTheme by mutableStateOf(ThemeDetector.isDarkThemeUsed)
    ThemeDetector.registerListener { isDarkTheme = it }
    MaterialTheme(
        colorScheme = if (isDarkTheme) darkColorScheme() else lightColorScheme()
    )
*/

object DarkModeDetector {
    private val detector = OsThemeDetector.getDetector()

    val isDarkThemeUsed: Boolean
        get() = detector.isDark

    fun registerListener(listener: (Boolean) -> Unit) {
        detector.registerListener { isDark -> listener(isDark) }
    }
}