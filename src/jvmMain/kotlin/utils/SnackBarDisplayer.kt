package utils

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import moe.tlaster.precompose.koin.koinViewModel
import org.koin.compose.getKoin
import viewmodel.MainViewModel

@Composable
fun SnackBarDisplayer() {
    val vm: MainViewModel = koinViewModel()
    val snackbarHostState : SnackbarHostState = getKoin().get()
    LaunchedEffect(snackbarHostState) {
        vm.snackbarEvent.collect { event ->
            event?.let {
                val result = snackbarHostState.showSnackbar(
                    message = it.message,
                    actionLabel = it.actionLabel,
                    duration = it.duration,
                    withDismissAction = it.withDismissAction
                )
                when (result) {
                    SnackbarResult.ActionPerformed -> vm.onSnackbarAction(it.action)
                    SnackbarResult.Dismissed -> {
                        vm.onSnackbarDismiss(it.dismissAction)
                    }
                }
                vm.onSnackbarShown()
            }
        }
    }
}
