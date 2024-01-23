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
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Brands
import compose.icons.fontawesomeicons.brands.Github
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import ui.components.InfoContainer
import ui.components.LinkText
import ui.components.PointerModifier
import ui.components.SubTitle
import ui.components.loadAppIcon
import utils.openUrlInBrowser
import utils.stringResource
import viewmodel.MainViewModel

@OptIn(ExperimentalResourceApi::class)
@Composable
fun About(vm: MainViewModel) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        Column(
            modifier = Modifier.fillMaxSize().padding(start = 32.dp, end = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(stringResource("app_name"), style = MaterialTheme.typography.headlineMedium)
            Image(
                painter = loadAppIcon(),
                contentDescription = null,
                modifier = Modifier.height(80.dp).clip(MaterialTheme.shapes.medium)
            )
            InfoContainer("A Kotlin Compose Desktop Template designed for Android developers transitioning to desktop projects. Features MVVM architecture, native functionalities, and internationalization support.")
            Text("By Elyahou G (לא -מתייאש)", style = MaterialTheme.typography.bodyMedium)
            OutlinedButton(
                onClick = { openUrlInBrowser("https://github.com/kdroidFilter/compose-desktop/") },
                modifier = PointerModifier
            ) {
                Icon(
                    FontAwesomeIcons.Brands.Github,
                    null,
                    modifier = Modifier.height(32.dp).padding(end = 8.dp)
                )
                Text(
                    "Star the project on GitHub ! ",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Text("or Follow me on : ")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                IconButton(
                    { openUrlInBrowser("https://ko-fi.com/lomityaesh") },
                    modifier = PointerModifier
                ) {
                    Image(
                        painterResource("assets/kofi.png"),
                        null,
                        modifier = Modifier.height(50.dp)
                    )
                }
                IconButton(
                    { openUrlInBrowser("https://mitmachim.top/user/%D7%9C%D7%90-%D7%9E%D7%AA%D7%99%D7%99%D7%90%D7%A9/topics") },
                    modifier = PointerModifier
                ) {
                    Image(
                        painterResource("assets/mitmachim.png"),
                        null,
                        modifier = Modifier.height(50.dp)
                    )
                }
            }
            Column(Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Bottom) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("Made with \uD83D\uDC99 with ")
                    LinkText(
                        "Kotlin Multiplateform",
                        "https://kotlinlang.org/docs/multiplatform.html"
                    )
                    Text(" & ")
                    LinkText(
                        "Compose Multiplateform",
                        "https://jetbrains.com/lp/compose-multiplatform"
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("Packaged with ")
                    LinkText("Conveyor", "https://www.hydraulic.dev/")
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    "Version: " + vm.currentVersion,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
