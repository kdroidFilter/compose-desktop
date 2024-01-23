package navigation


import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import enums.NavigationDestination
import moe.tlaster.precompose.navigation.Navigator
import ui.components.PointerModifier
import viewmodel.MainViewModel

@ExperimentalMaterialApi
@Composable
fun NavRail(vm: MainViewModel, navigator: Navigator) {
    NavigationRail {
        NavigationDestination.entries.forEach {
            if (it == NavigationDestination.License ||
                it == NavigationDestination.About ||
                it == NavigationDestination.ContactConfirmation ||
                it == NavigationDestination.FirstConfig
            ) return@forEach
            val isSelected = it.route == vm.currentRoute.value
            if (vm.wasConfig.collectAsState().value) return@forEach
            NavigationRailItem(
                icon = { Icon(imageVector = it.icon!!, contentDescription = it.name) },
                label = { Text(it.title) },
                onClick = { if(!isSelected) navigator.navigate(it.route) },
                selected = it.route == vm.currentRoute.value,
                modifier = PointerModifier
            )
        }
    }
}

