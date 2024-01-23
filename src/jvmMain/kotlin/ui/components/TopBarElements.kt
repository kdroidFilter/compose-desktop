package ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Campaign
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.Fullscreen
import androidx.compose.material.icons.rounded.FullscreenExit
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.SystemUpdateAlt
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import enums.NavigationDestination
import moe.tlaster.precompose.navigation.Navigator
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import utils.AsyncImage
import utils.loadImageBitmap
import utils.openUrlInBrowser
import utils.stringResource
import viewmodel.MainViewModel
import kotlin.system.exitProcess

class TopBarElements(val vm: MainViewModel, val navigator: Navigator) {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TopBar() {
        val material2 = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.surface,
            navigationIconContentColor = MaterialTheme.colorScheme.surface,
            actionIconContentColor = MaterialTheme.colorScheme.surface
        )
        val material3 = TopAppBarDefaults.centerAlignedTopAppBarColors()
        CenterAlignedTopAppBar(title = {
            Text(vm.appBarTitle.value, modifier = Modifier.padding(start = 64.dp))
        }, navigationIcon = { HomeButton(vm) }, actions = {
            UpdateButton()
            KofiPostButton(vm.kofiPosts)
            FullScreenButton()
            DarkThemeButton()
            VerticalButton()
            ExitButton()
        }, colors = if (vm.isMaterial3()) material3 else material2
        )
    }

    @Composable
    fun DarkThemeButton() {
        if (!vm.darkModeSwitch.value) return
        IconButton(onClick = { vm.toggleTheme() }, modifier = PointerModifier) {
            Icon(
                if (vm.useDarkTheme.value) Icons.Rounded.LightMode else Icons.Rounded.DarkMode, null
            )
        }
    }

    @Composable
    fun FullScreenButton() {
        val icon =
            if (vm.isNotFullScreen()) Icons.Rounded.Fullscreen else Icons.Rounded.FullscreenExit
        IconButton(modifier = PointerModifier, onClick = { vm.toggleFullScreen() }) {
            Icon(
                imageVector = icon, null
            )
        }
    }

    @OptIn(ExperimentalResourceApi::class)
    @Composable
    fun HomeButton(vm: MainViewModel) {
        val isRtl = vm.isCurrentLanguageRtl()
        val canGoBack =
            navigator.canGoBack.collectAsState(false).value && vm.currentRoute.value != NavigationDestination.Home.route
        val isLoading = vm.isLoading.collectAsState(false).value
        val backIcon = if (!isRtl) Icons.Rounded.ArrowBack else Icons.Rounded.ArrowForward
        val modifier = Modifier.size(24.dp)
        val contentDescription = stringResource("app_name")
        IconButton(
            modifier = if (canGoBack) PointerModifier else Modifier,
            onClick = { if (canGoBack) navigator.goBack() }) {
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = modifier
                    )
                }
                canGoBack -> {
                    Icon(
                        imageVector = backIcon,
                        contentDescription = contentDescription,
                        modifier = modifier
                    )
                }
                else -> {
                    Icon(
                        painterResource("AppIcon.png"),
                        contentDescription = contentDescription,
                        modifier = modifier
                    )
                }
            }
        }
    }


    @Composable
    fun VerticalButton() {
        var isOpened by remember { mutableStateOf(false) }
        Box {
            IconButton(modifier = PointerModifier, onClick = {
                isOpened = true
            }) { Icon(imageVector = Icons.Rounded.MoreVert, null) }
            DropdownMenu(expanded = isOpened, onDismissRequest = { isOpened = !isOpened }) {
                val menuItems = listOf(NavigationDestination.About, NavigationDestination.License)
                menuItems.forEach() { item ->
                    DropdownMenuItem(modifier = PointerModifier, text = {
                        Row {
                            Icon(imageVector = item.icon!!, contentDescription = item.name)
                            Text(item.title, Modifier.padding(start = 4.dp))
                        }
                    }, onClick = {
                        navigator.navigate(item.route)
                        isOpened = false
                    })
                }
            }
        }
    }

    @Composable
    fun ExitButton() {
        if (!vm.isNotFullScreen()) {
            IconButton(modifier = PointerModifier, onClick = {
                exitProcess(0)
            }) {
                Icon(Icons.Rounded.Close, null)
            }
        }
    }

    @Composable
    fun UpdateButton() {
        if (vm.showUpdateButton.value) {
            IconButton({ vm.showUpdater() }, modifier = PointerModifier) {
                Icon(Icons.Rounded.SystemUpdateAlt, null)
            }
        }
    }

    @Composable
    fun KofiPostButton(posts: List<data.model.KofiPost>) {
        if(!vm.feedSwitch.value) return
        var expanded by remember { mutableStateOf(false) }

        Box {
            // Bouton pour ouvrir/fermer le menu déroulant
            IconButton(onClick = {
                expanded = !expanded
                if (posts.isEmpty()) {
                    vm.showSnackbar(stringResource("error_not_internet_connection"))
                    vm.kofiPostFetcher()
                }
            },
                modifier = PointerModifier) {
                Icon(Icons.Rounded.Campaign, contentDescription = "Localized description")
            }

            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                // Menu déroulant
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    posts.forEach { post ->
                        DropdownMenuItem(
                            onClick = { openUrlInBrowser("https://ko-fi.com/" + post.postUrl) },
                            text = {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(text = post.postTitle, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
                                    Text(text = post.timeAgo, textAlign = TextAlign.End, modifier = Modifier.fillMaxWidth(), style = MaterialTheme.typography.bodySmall)
                                }
                            },
                            leadingIcon = {
                                AsyncImage(
                                    load = { loadImageBitmap(vm, post.imageUrl) },
                                    painterFor = { BitmapPainter(it) },
                                    contentDescription = "Post Image",
                                    modifier = Modifier
                                        .size(50.dp)
                                        .aspectRatio(1f)
                                        .clipToBounds(),
                                    contentScale = ContentScale.Crop
                                )
                            },
                            modifier = PointerModifier
                        )
                    }
                }
            }
        }
    }
}

