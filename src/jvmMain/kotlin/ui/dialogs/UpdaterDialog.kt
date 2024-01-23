package ui.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.components.InfoContainer
import ui.components.PointerModifier
import utils.openUrlInBrowser
import utils.stringResource
import viewmodel.MainViewModel

@Composable
fun UpdaterDialog(vm: MainViewModel) {
    val isUpdatedRequired = vm.isUpdateRequired.value
    if (isUpdatedRequired) {
        AlertDialog(
            onDismissRequest = { vm.hideUpdater() },
            title = { Text(stringResource("update_available")) },
            text = { 
                Column {
                    Text(stringResource("new_version_available"))
                    Spacer(Modifier.height(16.dp))
                    InfoContainer(vm.appVersion.value.changelog)
                } },
            confirmButton = {
                Spacer(Modifier.width(8.dp))
                Button(modifier = PointerModifier, onClick = { openUrlInBrowser(vm.appVersion.value.url); vm.hideUpdater() }) {
                    Text(stringResource("update"))
                }
            },
            dismissButton = {
                OutlinedButton(modifier = PointerModifier,onClick = { vm.hideUpdater() }) {
                    Text(stringResource("later"))
                }
            }
        )
    }
}