package ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import utils.openUrlInBrowser
import utils.stringResource
import viewmodel.MainViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun KofiButton(vm: MainViewModel) {
    var isHovered by remember { mutableStateOf(false) }
    val isActivated = vm.kofiButtonStatus.value

    if (!isActivated) {
        return
    }

    Row(
        modifier = Modifier
            .padding(16.dp)
            .onPointerEvent(PointerEventType.Move) {
            }
            .onPointerEvent(PointerEventType.Enter) {
                isHovered = true
            }
            .onPointerEvent(PointerEventType.Exit) {
                isHovered = false
            }
    ) {
        FloatingActionButton(
            onClick = { openUrlInBrowser("https://ko-fi.com/lomityaesh") },
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = PointerModifier
                .height(60.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 12.dp) // Ajoutez un padding pour l'expansion
            ) {
                Image(
                    painterResource("kofi.webp"),
                    null,
                    modifier = Modifier.size(50.dp),
                    contentScale = ContentScale.Crop
                )
                AnimatedVisibility(
                    visible = isHovered,
                    enter = fadeIn() + expandHorizontally(), // Animation d'entrée
                    exit = fadeOut() + shrinkHorizontally() // Animation de sortie
                ) {
                    Text(
                        stringResource("kofi_button"),
                        modifier = Modifier.padding(start = 8.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}
