import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.compose.App
import data.manager.PreferencesManager
import data.manager.TrayIconManager
import data.repository.ColorRepository
import data.repository.NotesDatabaseRepository
import data.repository.SettingsTabsRepository
import data.repository.TextRepository
import data.repository.ThemeModeRepository
import data.repository.VersionRepository
import data.repository.WindowsPlacementRepository
import dorkbox.systemTray.Checkbox
import dorkbox.systemTray.Menu
import dorkbox.systemTray.MenuItem
import dorkbox.systemTray.Separator
import dorkbox.systemTray.SystemTray
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
import viewmodel.NotesViewModel
import java.awt.Dimension
import java.util.Locale


fun main() = application() {
    val appIcon = loadAppIcon()
    val vm = rememberSaveable { MainViewModel(
        localizationRepository = Localization,
        preferencesManager = PreferencesManager,
        colorRepository = ColorRepository,
        versionReposition = VersionRepository,
        windowsPlacementRepository = WindowsPlacementRepository,
        themeModeRepository = ThemeModeRepository,
        textRepository = TextRepository,
        settingsTabsRepository = SettingsTabsRepository,
        applicationScope = this
    ) }
    Locale.setDefault(Locale(vm.currentLanguage.value))


    val notesViewModel = NotesViewModel(NotesDatabaseRepository)
    Window(
        onCloseRequest = vm.exit(),
        title = stringResource("app_name"),
        state = vm.windowsState,
        icon = appIcon,
        undecorated = vm.isMaterialWindows,
        alwaysOnTop = vm.alwaysOnTopMode.value,
        visible = vm.isWindowVisible.collectAsState().value
    ) {
        val tray = SystemTray.get()
        val menu = tray.menu
        val trayIcon = this::class.java.classLoader.getResource("AppIcon.png")
        tray.setImage(trayIcon)

        menu.add(MenuItem("Open") {
            vm.setWindowVisibility(true)
        })
        menu.add(MenuItem("hide") {
            // tray.setEnabled(false)
            run { vm.setWindowVisibility(false) }
        })

        // add a checkbox
        menu.add(Checkbox("Checkbox Item") {
            println("Checkbox Item: $it")
        })

        // add a separator
        menu.add(Separator())

        // add a submenu
        val submenu = Menu("Submenu")
        submenu.add(MenuItem("exit") {
           exitApplication()
        })
        menu.add(submenu)
        window.minimumSize = Dimension(680, 370)
        App(vm) {
            val snackbarHostState = remember { SnackbarHostState() }
            val navigator = rememberNavigator()
            SnackBarDisplayer(vm, snackbarHostState)
            Scaffold(
                topBar = { WindowDraggableArea { TopBarElements(vm, navigator).TopBar() } },
                floatingActionButton = { KofiButton(vm) },
                snackbarHost = { SnackbarHost(snackbarHostState) },
            ) { paddingValues ->
                Surface(
                    Modifier.padding(paddingValues).padding(16.dp).fillMaxSize()
                ) {
                    NavGraph(vm, navigator, notesViewModel)
                    UpdaterDialog(vm)
                }
            }
        }
    }

}
