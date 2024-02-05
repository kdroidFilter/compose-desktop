package navigation

import androidx.compose.foundation.layout.Row
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import enums.NavigationDestination
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.Navigator
import org.koin.compose.getKoin
import ui.screens.About
import ui.screens.FirstConfig
import ui.screens.Home
import ui.screens.License
import ui.screens.Settings
import ui.screens.contact.ContactConfirmation
import ui.screens.contact.ContactHome
import viewmodel.MainViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NavGraph() {
    val vm: MainViewModel = koinViewModel()
    val navigator: Navigator = getKoin().get()
    Row {
        NavRail(vm, navigator)
        NavHost(
            navigator = navigator,
            initialRoute =
            if (vm.wasConfig.value) NavigationDestination.FirstConfig.route else NavigationDestination.Home.route
        ) {
            NavigationDestination.entries.forEach { destination ->
                scene(destination.route) {
                    when (destination) {
                        NavigationDestination.FirstConfig -> FirstConfig()
                        NavigationDestination.Home -> Home()
                        NavigationDestination.About -> About()
                        NavigationDestination.Contact -> ContactHome()
                        NavigationDestination.ContactConfirmation -> ContactConfirmation()
                        NavigationDestination.Settings -> Settings()
                        NavigationDestination.License -> License()

                    }
                    vm.setAppBarTitle(destination.title)
                    vm.setCurrentRoute(destination.route)
                }
            }
        }
    }
}
