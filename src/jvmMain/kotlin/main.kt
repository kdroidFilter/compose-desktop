import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.compose.App
import data.repository.NotesDatabaseRepository
import di.AppModule
import dorkbox.systemTray.Checkbox
import dorkbox.systemTray.Menu
import dorkbox.systemTray.MenuItem
import dorkbox.systemTray.Separator
import dorkbox.systemTray.SystemTray
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.stateholder.LocalStateHolder
import moe.tlaster.precompose.stateholder.StateHolder
import moe.tlaster.precompose.viewmodel.viewModel
import navigation.NavGraph
import org.koin.compose.getKoin
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.parameter.parametersOf
import ui.components.KofiButton
import ui.components.TopBarElements
import ui.components.loadAppIcon
import ui.dialogs.UpdaterDialog
import utils.SnackBarDisplayer
import utils.stringResource
import viewmodel.MainViewModel
import viewmodel.NotesViewModel
import java.awt.Dimension
import java.util.Locale


fun main() = application() {
    val appModule = AppModule
    startKoin {
        modules(appModule.appModule, appModule.navigatorModule)
    }

    val stateHolder = remember { StateHolder() }
    CompositionLocalProvider(LocalStateHolder provides stateHolder) {
        val vm: MainViewModel = koinViewModel { parametersOf(this) }
        Locale.setDefault(Locale(vm.currentLanguage.collectAsState().value))
        val notesViewModel = viewModel { NotesViewModel(NotesDatabaseRepository) }
        val appIcon = loadAppIcon()
        Window(
            onCloseRequest = vm.exit(),
            title = stringResource("app_name"),
            state = vm.windowsState,
            icon = appIcon,
            undecorated = vm.isMaterialWindows,
            alwaysOnTop = vm.alwaysOnTopMode.collectAsState().value,
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
                val navigator: Navigator = getKoin().get()
                SnackBarDisplayer(vm, snackbarHostState)
                Scaffold(
                    topBar = { WindowDraggableArea { TopBarElements(vm, navigator).TopBar() } },
                    floatingActionButton = { KofiButton(vm) },
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                ) { paddingValues ->
                    Surface(
                        Modifier.padding(paddingValues).padding(16.dp).fillMaxSize()
                    ) {
                        NavGraph(notesViewModel)
                        UpdaterDialog(vm)
                    }
                }
            }
        }

    }
}
