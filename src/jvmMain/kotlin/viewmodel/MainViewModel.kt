package viewmodel

import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import data.api.KofiPostScrapper
import data.api.TextScrapper
import data.api.VersionApi
import data.implementation.KofiPostScrapperImpl
import data.implementation.TextScrapperImpl
import data.implementation.VersionApiImpl
import data.manager.PreferencesManager
import data.model.AppVersion
import data.model.ColorThemeModel
import data.model.KofiPost
import data.model.PaletteThemeMode
import data.model.SnackbarEvent
import data.model.WindowsPlacementModel
import data.repository.ColorRepository
import data.repository.SettingsTabsRepository
import data.repository.TextRepository
import data.repository.ThemeModeRepository
import data.repository.VersionRepository
import data.repository.WindowsPlacementRepository
import enums.AppBarMode
import enums.NavigationDestination
import enums.ThemeMode
import enums.WindowsPlacementConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import utils.DarkModeDetector
import utils.Localization
import utils.TrustAllCertsHttpClient
import utils.stringResource
import kotlin.system.exitProcess

class MainViewModel(
    private val localizationRepository: Localization,
    private val preferencesManager: PreferencesManager,
    private val colorRepository: ColorRepository,
    private val themeModeRepository: ThemeModeRepository,
    private val versionReposition: VersionRepository,
    private val windowsPlacementRepository: WindowsPlacementRepository,
    private val textRepository: TextRepository,
    private val settingsTabsRepository : SettingsTabsRepository
) : ViewModel() {

    val client = TrustAllCertsHttpClient.client

    //FIRST CONFIG
    private val _wasConfig = MutableStateFlow(preferencesManager.wasConfig())
    val wasConfig = _wasConfig.asStateFlow()
    fun configDone() {
        preferencesManager.configDone()
        _wasConfig.value = false
    }

    private val _name = mutableStateOf("")
    val name = _name
    fun setName(name: String) {
        _name.value = name
    }

    fun getName(): String {
        return preferencesManager.getName()
    }

    fun saveName() {
        preferencesManager.saveName(_name.value)
    }

    //UPDATER SYSTEM
    val currentVersion = versionReposition.getCurrentVersion()
    private val _isUpdateRequired = mutableStateOf(false)
    val isUpdateRequired = _isUpdateRequired

    val showUpdateButton = mutableStateOf(false)
    val appVersion = mutableStateOf(AppVersion())

    fun checkForUpdates() {
        val versionApi: VersionApi = VersionApiImpl(client)
        viewModelScope.launch(Dispatchers.IO) {
            try {
           //     if (currentVersion == "debug") return@launch
                val latestVersion = versionApi.getCurrentVersion()
                if (latestVersion.version == "N/A") return@launch
                _isUpdateRequired.value = currentVersion != latestVersion.version
                showUpdateButton.value = currentVersion != latestVersion.version
                appVersion.value = latestVersion
            } catch (e: Exception) {
                println("Error : $e")
            }
        }
    }

    fun hideUpdater() {
        _isUpdateRequired.value = false
    }

    fun showUpdater() {
        _isUpdateRequired.value = true
    }

    //ROUTING SYSTEM
    private var _currentRoute = mutableStateOf(NavigationDestination.Home.route)
    val currentRoute = _currentRoute
    fun setCurrentRoute(route: String) {
        _currentRoute.value = route
    }


    // SNACKBAR SYSTEM
    private val _snackbarEvent = MutableStateFlow<SnackbarEvent?>(null)
    val snackbarEvent: StateFlow<SnackbarEvent?> = _snackbarEvent.asStateFlow()
    fun showSnackbar(
        message: String,
        duration: SnackbarDuration = SnackbarDuration.Short,
        actionLabel: String? = null,
        action: () -> Unit = {},
        withDismissAction: Boolean = true,
        dismissAction: () -> Unit = {}
    ) {
        _snackbarEvent.value =
            SnackbarEvent(message, duration, actionLabel, action, withDismissAction, dismissAction)
    }

    fun onSnackbarShown() {
        _snackbarEvent.value = null
    }

    fun onSnackbarAction(action: () -> Unit) {
        action()
    }

    fun onSnackbarDismiss(action: () -> Unit) {
        action()
    }

    //APPBAR TITLE
    private var _appBarTitle = mutableStateOf(stringResource("app_name"))
    val appBarTitle = _appBarTitle
    fun setAppBarTitle(title: String) {
        _appBarTitle.value = title
    }

    //APPBAR ICON LOADER
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Fonction pour déclencher le chargement
    fun setLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }

    //SETTINGS TABS SELECTOR
    val settingTabs = settingsTabsRepository.getTabs()

    private val _selectedTabIndex = mutableStateOf(0)
    val selectedTabIndex = _selectedTabIndex

    fun onTabSelected(index: Int) {
        _selectedTabIndex.value = index
    }

    //APPBAR MODE
    private fun getAppBarMode(): String = preferencesManager.getAppBarMode()
    private val _appBarMode = mutableStateOf(getAppBarMode())
    fun getAppBarModeName(): String {
        return when (_appBarMode.value) {
            AppBarMode.MATERIAL3.name -> AppBarMode.MATERIAL3.text
            else -> AppBarMode.MATERIAL2.text
        }
    }

    fun getAllAppBarMode(): List<AppBarMode> = AppBarMode.entries
    fun setAppBarMode(mode: String) {
        preferencesManager.setAppBarMode(mode)
        _appBarMode.value = mode
    }

    fun isMaterial3(): Boolean = _appBarMode.value == AppBarMode.MATERIAL3.name

    // DARK THEME FUNCTION
    private fun useDarKTheme() =
        themeModeRepository.useDarKTheme(preferencesManager.getCurrentTheme())

    private var _useDarkTheme = mutableStateOf(useDarKTheme())
    val useDarkTheme = _useDarkTheme
    fun setTheme(theme: String) {
        preferencesManager.setTheme(theme)
        _useDarkTheme.value = useDarKTheme()
    }

    fun getCurrentTheme(): ThemeMode {
        return themeModeRepository.getCurrentTheme(preferencesManager.getCurrentTheme())
    }

    fun getAllModes(): List<ThemeMode> = themeModeRepository.getAllModes()
    fun toggleTheme() {
        useDarkTheme.value = !useDarkTheme.value
    }

    fun registerDarkThemeListener() {
        DarkModeDetector.registerListener {
            if (themeModeRepository.isAutoMode(preferencesManager.getCurrentTheme())) {
                _useDarkTheme.value = it
            }
        }
    }

    //DARKMODE SWITCH MANAGER
    private val _darkModeSwitch = mutableStateOf(preferencesManager.getDarkModeSwitch())
    val darkModeSwitch = _darkModeSwitch
    fun setDarkModeSwitch(value: Boolean) {
        preferencesManager.setDarkModeSwitch(value)
        _darkModeSwitch.value = value
    }

    //FEED SWITCH MANAGER
    private val _feedSwitch = mutableStateOf(preferencesManager.getFeedSwitch())
    val feedSwitch = _feedSwitch
    fun setFeedSwitch(value: Boolean) {
        preferencesManager.setFeedSwitch(value)
        _feedSwitch.value = value
    }

    // LANGUAGE FUNCTION
    private var _isRtl = localizationRepository.isCurrentLanguageRtl()
    fun isCurrentLanguageRtl() = _isRtl
    private var _currentLanguage = localizationRepository.currentLanguageCode()
    fun getCurrentLanguage(): String = _currentLanguage
    fun getCurrentLanguageName() = localizationRepository.currentLanguage()!!.name
    fun changeLanguage(key: String) {
        localizationRepository.setLanguage(key)
        _currentLanguage = localizationRepository.currentLanguageCode()
        preferencesManager.setLanguage(key)
        showSnackbar(
            stringResource("language_change_success_message"),
            actionLabel = stringResource("exit_action"),
            action = { exitProcess(0) },
            duration = SnackbarDuration.Long
        )
    }

    //SET WINDOWS PLACEMENT BY DEFAULT
    fun getWindowsPlacements(): List<WindowsPlacementModel> =
        windowsPlacementRepository.getWindowsPlacements()

    private var _windowsPlacement by mutableStateOf(
        WindowsPlacementConfig.getPlacementByProperty(
            preferencesManager.getWindowPlacementConfig()
        ) ?: WindowPlacement.Floating
    )
    private var _windowPlacementConfig by mutableStateOf(preferencesManager.getWindowPlacementConfig())
    val windowPlacementConfig get() = _windowPlacementConfig
    fun saveWindowPlacementConfig(placement: String) {
        preferencesManager.saveWindowPlacementConfig(placement)
        _windowPlacementConfig = placement
        showSnackbar(
            message = "At the next start, the app will be start in $placement mode"
        )
    }

    private val _windowsState = WindowState(
        placement = _windowsPlacement,
        position = WindowPosition.Aligned(Alignment.Center),
        size = DpSize(1280.dp, 720.dp)
    )
    val windowsState = _windowsState
    private var _windowsPlacementToggle by
    mutableStateOf(WindowsPlacementConfig.getPropertyByPlacement(_windowsPlacement) != WindowsPlacementConfig.FULLSCREEN.property)

    //FULLSCREEN TOGGLE
    fun toggleFullScreen() {
        if (_windowsPlacementToggle)
            _windowsState.placement = WindowPlacement.Fullscreen
        else _windowsState.placement = WindowPlacement.Floating
        _windowsPlacementToggle = !_windowsPlacementToggle
    }

    fun isNotFullScreen() = _windowsPlacementToggle

    //THEME COLOR Style
    fun getColors() = colorRepository.getColors()
    private var _themeColorName by mutableStateOf(preferencesManager.getThemeColor())
    fun setThemeColor(color: String) {
        preferencesManager.setThemeColor(color)
        _themeColorName = color
    }

    fun getCurrentThemeColor(): ColorThemeModel? {
        return getColors().find { it.name == _themeColorName }
    }

    // THEME PALETTE STYLE
    fun getPalette() = colorRepository.getPalette()
    private var _themePaletteName by mutableStateOf(preferencesManager.getThemePalette())
    fun setThemePalette(palette: String) {
        preferencesManager.setThemePalette(palette)
        _themePaletteName = palette
    }

    fun getCurrentThemePalette(): PaletteThemeMode? {
        return getPalette().find { it.name == _themePaletteName }
    }

    //KOFI BUTTON MANAGER
    private var _kofiButtonStatus = mutableStateOf(preferencesManager.getKofiButtonStatus())
    fun setKofiButtonStatus(status: Boolean) {
        preferencesManager.setKofiButtonStatus(status)
        _kofiButtonStatus.value = status
    }

    val kofiButtonStatus = _kofiButtonStatus

    // CLEAR SETTINGS
    fun clearSettings() {
        preferencesManager.clear()
    }

    //KOFI POST FETCHER
    private val _kofiPosts = mutableStateListOf<KofiPost>()
    val kofiPosts = _kofiPosts

    fun kofiPostFetcher() {
        val fetcher: KofiPostScrapper = KofiPostScrapperImpl()
        viewModelScope.launch(Dispatchers.IO) {
            fetcher.fetch(client, _kofiPosts)
        }
    }

    //README FETCHER
    private val _readme = mutableStateOf("")
    private val _license = mutableStateOf("")
    fun textFetcher(url: String, permanentValue: String, arg: MutableState<String>) {
        val scrapper: TextScrapper = TextScrapperImpl()
        viewModelScope.launch(Dispatchers.IO) {
            scrapper.fetch(client, url, arg)
            saveTextOffline(arg.value, permanentValue)
        }
    }

    fun saveTextOffline(content: String, value: String) {
        preferencesManager.saveTextOffline(content, value)
    }

    fun getReadme(): String {
        return if (_readme.value.isEmpty()) preferencesManager.getTextOffline("readmeOffline") else _readme.value
    }

    fun getLicense() = textRepository.getLicense

    init {
      //  if(!wasConfig.value) checkForUpdates()
        kofiPostFetcher()
        textFetcher(textRepository.readmeUrl, "readmeOffline", _readme)
    }

}