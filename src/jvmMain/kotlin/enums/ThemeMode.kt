package enums

import utils.stringResource
import utils.DarkModeDetector

enum class ThemeMode(
    val mode: Boolean,
    val value : String,
    val text : String = value
) {
    LIGHT(false, "light", stringResource("light_mode")),
    DARK(true, "dark", stringResource("dark_mode")),
    AUTO(DarkModeDetector.isDarkThemeUsed, "auto", stringResource("auto_mode"))
}