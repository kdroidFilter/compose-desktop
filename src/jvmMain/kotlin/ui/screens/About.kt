package ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import ui.components.InfoContainer
import ui.components.LinkText
import ui.components.PointerModifier
import ui.components.SubTitle
import ui.components.loadAppIcon
import utils.openUrlInBrowser
import utils.stringResource
import viewmodel.MainViewModel

@Composable
fun About(vm: MainViewModel) {
    Column(modifier = Modifier.fillMaxSize().padding(start = 32.dp, end = 32.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(stringResource("app_name"), style = MaterialTheme.typography.headlineMedium)
        Image(
            painter = loadAppIcon(),
            contentDescription = null,
            modifier = Modifier.height(80.dp).clip(MaterialTheme.shapes.medium)
        )
        InfoContainer("A Kotlin Compose Desktop Template designed for Android developers transitioning to desktop projects. Features MVVM architecture, native functionalities, and internationalization support.")
        Column(Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Bottom) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text("Made with \uD83D\uDC99 with ")
                LinkText("Kotlin Multiplateform", "https://kotlinlang.org/docs/multiplatform.html")
                Text(" & ")
                LinkText(
                    "Compose Multiplateforme",
                    "https://jetbrains.com/lp/compose-multiplatform"
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text("Packaged with ")
                LinkText("Conveyor", "https://www.hydraulic.dev/")
            }
            Spacer(Modifier.height(8.dp))
            Text("Version: " + vm.currentVersion, textAlign = TextAlign.Center, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.fillMaxWidth())
        }
    }
}
