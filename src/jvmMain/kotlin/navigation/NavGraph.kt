package navigation

import androidx.compose.foundation.layout.Row
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import enums.NavigationDestination
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.Navigator
import ui.screens.About
import ui.screens.FirstConfig
import ui.screens.Home
import ui.screens.License
import ui.screens.Settings
import ui.screens.contact.ContactConfirmation
import ui.screens.contact.ContactHome
import viewmodel.MainViewModel
import viewmodel.NotesViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NavGraph(vm: MainViewModel, navigator: Navigator, notesViewModel: NotesViewModel) {
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
                        NavigationDestination.FirstConfig -> FirstConfig(vm, navigator)
                        NavigationDestination.Home -> Home(vm, notesViewModel, navigator)
                        NavigationDestination.About -> About(vm)
                        NavigationDestination.Contact -> ContactHome(vm, navigator)
                        NavigationDestination.ContactConfirmation -> ContactConfirmation()
                        NavigationDestination.Settings -> Settings(vm)
                        NavigationDestination.License -> License(vm)

                    }
                    vm.setAppBarTitle(destination.title)
                    vm.setCurrentRoute(destination.route)
                }
            }
        }
    }
}
