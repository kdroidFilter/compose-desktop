
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
import data.manager.TrayIconManager
import di.AppModule
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.stateholder.LocalStateHolder
import moe.tlaster.precompose.stateholder.StateHolder
import navigation.NavGraph
import org.koin.compose.getKoin
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.parameter.parametersOf
import ui.components.KofiButton
import ui.components.TopBarElements
import ui.components.loadAppIcon
import ui.dialogs.UpdaterDialog
import utils.InstanceManager
import utils.SnackBarDisplayer
import utils.stringResource
import viewmodel.MainViewModel
import java.awt.Dimension
import java.util.Locale


fun main() = application {
    InstanceManager.checkForExistingInstance()

    val appModule = AppModule
    startKoin {
        modules(
            appModule.appModule,
            appModule.navigatorModule,
            appModule.snackbarHostState,
            appModule.notesModule,
            appModule.contactModule,
            appModule.trayModule
        )
    }

    val stateHolder = remember { StateHolder() }
    CompositionLocalProvider(LocalStateHolder provides stateHolder) {
        val vm: MainViewModel = koinViewModel { parametersOf(this) }
        InstanceManager.showActiveInstanceWindow()
        val trayIconManager: TrayIconManager = getKoin().get()
        Locale.setDefault(Locale(vm.currentLanguage.collectAsState().value))
        val appIcon = loadAppIcon()
        Window(
            onCloseRequest = { vm.closeAppAction().invoke() },
            title = stringResource("app_name"),
            state = vm.windowsState,
            icon = appIcon,
            undecorated = vm.isMaterialWindows,
            alwaysOnTop = vm.alwaysOnTopMode.collectAsState().value,
            visible = vm.isWindowVisible.collectAsState().value
        ) {
            window.minimumSize = Dimension(680, 370)
            App {
                val snackbarHostState: SnackbarHostState = getKoin().get()
                SnackBarDisplayer()
                Scaffold(
                    topBar = { WindowDraggableArea { TopBarElements().TopBar() } },
                    floatingActionButton = { KofiButton() },
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                ) { paddingValues ->
                    Surface(
                        Modifier.padding(paddingValues).padding(16.dp).fillMaxSize()
                    ) {
                        NavGraph()
                        UpdaterDialog()
                    }
                }
            }
        }
    }
}