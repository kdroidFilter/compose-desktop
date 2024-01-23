package data.manager

import com.russhwolf.settings.PreferencesSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set
import enums.AppBarMode
import enums.ThemeMode
import enums.WindowsPlacementConfig
import java.util.prefs.Preferences

object PreferencesManager {
    private val delegate: Preferences = Preferences.userRoot().node(this::class.java.name)
    val settings: Settings = PreferencesSettings(delegate)

    fun clear() {
        settings.clear()
    }

    //FIRST CONFIG
    fun wasConfig() = settings.get<Boolean>("wasConfig") ?: true
    fun configDone() {
        settings["wasConfig"] = false
    }
    fun saveName(name: String) {
        settings["name"] = name
    }

    fun getName() = settings.get<String>("name") ?: ""

    //APP BAR MODE
    fun getAppBarMode(): String = settings.get<String>("appBarMode") ?: AppBarMode.MATERIAL3.name
    fun setAppBarMode(mode: String) {
        settings["appBarMode"] = mode
    }

    //DARK/LIGHT THEME MODE
    fun getCurrentTheme(): String {
        return settings.get<String>("currentTheme") ?: ThemeMode.AUTO.value
    }

    fun setTheme(theme: String) {
        settings["currentTheme"] = theme
    }

    //DARK/LIGHT SWITCH BUTTON
    fun setDarkModeSwitch(value: Boolean) {
        settings["darkModeSwitch"] = value
    }

    fun getDarkModeSwitch() = settings.get<Boolean>("darkModeSwitch") ?: true

    //FEED SWITCH BUTTON

    fun setFeedSwitch(value: Boolean) {
        settings["feedSwitch"] = value
    }

    fun getFeedSwitch() = settings.get<Boolean>("feedSwitch") ?: true

    //LANGUAGE SELECTION
    fun setLanguage(languageCode: String) {
        settings["language"] = languageCode
    }

    fun isLanguageSet() = settings.get<String>("language") != null
    fun getCurrentLanguage() = settings.get<String>("language")

    //WINDOWS PLACEMENT
    fun saveWindowPlacementConfig(placement: String) {
        settings["windowPlacement"] = placement
    }

    fun isWindowPlacementSet() = settings.get<WindowsPlacementConfig>("windowPlacement") != null
    fun getWindowPlacementConfig() =
        settings.get<String>("windowPlacement") ?: WindowsPlacementConfig.FLOATING.property

    //THEME COLOR STYLE
    fun setThemeColor(colorName: String) {
        settings["themeColor"] = colorName
    }

    fun getThemeColor() = settings.get<String>("themeColor") ?: "Bright Sky Blue"

    //THEME PALETTE STYLE
    fun setThemePalette(paletteName: String) {
        settings["themePalette"] = paletteName
    }

    fun getThemePalette() = settings.get<String>("themePalette") ?: "Vibrant"

    //KOFI BUTTON STATUS
    fun setKofiButtonStatus(status: Boolean) {
        settings["kofiButtonStatus"] = status
    }

    fun getKofiButtonStatus() = settings.get<Boolean>("kofiButtonStatus") ?: true

    //Save Readme Offline
    fun saveTextOffline(content: String, value: String) {
        if (!content.isEmpty()) {
            settings[value] = content
        }
    }
    fun getTextOffline(value: String) = settings.get<String>(value) ?: ""
}