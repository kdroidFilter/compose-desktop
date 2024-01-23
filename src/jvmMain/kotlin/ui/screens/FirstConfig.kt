package ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import enums.NavigationDestination
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator
import ui.components.InfoContainer
import utils.stringResource
import viewmodel.MainViewModel

@Composable
fun FirstConfig(vm: MainViewModel, navigator: Navigator) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center) {
        InfoContainer(stringResource("welcome_message"))
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = vm.name.value,
                onValueChange = { vm.setName(it) },
                placeholder = { Text(stringResource("enter_name")) },
                label = { Text(stringResource("name")) },
                maxLines = 1
            )
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = {
                    vm.saveName()
                    vm.configDone()
                    navigator.navigate(
                        NavigationDestination.Home.route,
                        NavOptions(launchSingleTop = true)
                    )
                }
            ) {
                Text(stringResource("end_configuration"))
            }
        }
    }
}