
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowScope
import androidx.compose.ui.window.application
import com.example.compose.App
import data.manager.PreferencesManager
import data.repository.ColorRepository
import data.repository.SettingsTabsRepository
import data.repository.TextRepository
import data.repository.ThemeModeRepository
import data.repository.VersionRepository
import data.repository.WindowsPlacementRepository
import enums.WindowsTheme
import moe.tlaster.precompose.navigation.rememberNavigator
import navigation.NavGraph
import ui.components.KofiButton
import ui.components.TopBarElements
import ui.components.loadAppIcon
import ui.dialogs.UpdaterDialog
import utils.Localization
import utils.SnackBarDisplayer
import utils.stringResource
import viewmodel.MainViewModel
import java.awt.Dimension
import java.util.Locale

fun main() = application() {
    val appIcon = loadAppIcon()
    val vm = MainViewModel(
        localizationRepository = Localization,
        preferencesManager = PreferencesManager,
        colorRepository = ColorRepository,
        versionReposition = VersionRepository,
        windowsPlacementRepository = WindowsPlacementRepository,
        themeModeRepository = ThemeModeRepository,
        textRepository = TextRepository,
        settingsTabsRepository = SettingsTabsRepository,
        applicationScope = this
    )
    Locale.setDefault(Locale(vm.currentLanguage.value))
    Window(
        onCloseRequest = vm.exit(),
        title = stringResource("app_name"),
        state = vm.windowsState,
        icon = appIcon,
        undecorated = vm.isMaterialWindows,

    ) {
        window.minimumSize = Dimension(680,370)
        App(vm) {
            val snackbarHostState = remember { SnackbarHostState() }
            val navigator = rememberNavigator()
            SnackBarDisplayer(vm, snackbarHostState)
            Scaffold(
                topBar = { WindowDraggableArea{ TopBarElements(vm, navigator).TopBar() }},
                floatingActionButton = { KofiButton(vm) },
                snackbarHost = { SnackbarHost(snackbarHostState) },
            ) { paddingValues ->
                Surface(
                    Modifier.padding(paddingValues).padding(16.dp).fillMaxSize()
                ) {
                    NavGraph(vm, navigator)
                    UpdaterDialog(vm)
                }
            }
        }
    }
}