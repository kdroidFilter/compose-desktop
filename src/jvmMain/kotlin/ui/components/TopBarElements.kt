package ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Campaign
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.Fullscreen
import androidx.compose.material.icons.rounded.FullscreenExit
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material.icons.rounded.Maximize
import androidx.compose.material.icons.rounded.Minimize
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.WindowMaximize
import compose.icons.fontawesomeicons.solid.WindowRestore
import enums.NavigationDestination
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.Navigator
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.getKoin
import utils.AsyncImage
import utils.loadImageBitmap
import utils.openUrlInBrowser
import utils.stringResource
import viewmodel.MainViewModel

class TopBarElements {

    private val littleIconModifier = Modifier.height(16.dp).padding(2.dp)
    private val iconButtonModifier = PointerModifier.height(20.dp).width(22.dp)

    private val bigIconModifier = Modifier.height(22.dp)
    private val bigIconButtonModifier = PointerModifier.height(28.dp)


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TopBar() {
        val vm: MainViewModel = koinViewModel()
        val material2 = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.surface,
            navigationIconContentColor = MaterialTheme.colorScheme.surface,
            actionIconContentColor = MaterialTheme.colorScheme.surface
        )
        val material3 = TopAppBarDefaults.centerAlignedTopAppBarColors()
        CenterAlignedTopAppBar(title = {
            Text(
                vm.appBarTitle.collectAsState().value,
                modifier = Modifier.padding(start = 64.dp)
            )
        }, navigationIcon = { HomeButton() }, actions = {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.End,
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    MinimizedButton()
                    MaximixedButton()
                    ExitButton()
                }
                Row {
                    UpdateButton()
                    KofiPostButton(vm.kofiPosts)
                    DarkThemeButton()
                    FullScreenButton()
                    VerticalButton()
                }
            }
        }, colors = if (vm.isMaterial3.collectAsState().value) material3 else material2
        )
    }

    @Composable
    fun DarkThemeButton() {
        val vm: MainViewModel = koinViewModel()
        if (!vm.darkModeSwitch.collectAsState().value) return
        IconButton(onClick = { vm.toggleTheme() }, modifier = bigIconButtonModifier) {
            Icon(
                if (vm.useDarkTheme.collectAsState().value) Icons.Rounded.LightMode else Icons.Rounded.DarkMode,
                null,
                bigIconModifier
            )
        }
    }

    @Composable
    fun FullScreenButton() {
        val vm: MainViewModel = koinViewModel()
        val icon =
            if (vm.isNotFullScreen()
                    .collectAsState().value
            ) Icons.Rounded.Fullscreen else Icons.Rounded.FullscreenExit
        IconButton(modifier = bigIconButtonModifier, onClick = { vm.toggleFullScreen() }) {
            Icon(
                imageVector = icon, null,
                bigIconModifier
            )
        }
    }

    @OptIn(ExperimentalResourceApi::class)
    @Composable
    fun HomeButton() {
        val vm: MainViewModel = koinViewModel()
        val navigator: Navigator = getKoin().get()

        val isRtl = vm.isCurrentLanguageRtl()
        val iconModifier = Modifier.height(22.dp)
        val canGoBack =
            navigator.canGoBack.collectAsState(false).value && vm.currentRoute.collectAsState().value != NavigationDestination.Home.route
        val isLoading = vm.isLoading.collectAsState(false).value
        val backIcon = if (!isRtl) Icons.Rounded.ArrowBack else Icons.Rounded.ArrowForward
        val contentDescription = stringResource("app_name")
        IconButton(
            modifier = if (canGoBack) bigIconButtonModifier else iconModifier,
            onClick = { if (canGoBack) navigator.goBack() }) {
            when {
                isLoading -> {
                    CircularProgressIndicator(Modifier.size(22.dp)
                    )
                }

                canGoBack -> {
                    Icon(
                        imageVector = backIcon,
                        contentDescription = contentDescription,
                        modifier = iconModifier

                    )
                }

                else -> {
                    Icon(
                        painterResource("AppIcon.png"),
                        contentDescription = contentDescription,
                        modifier = iconModifier
                    )
                }
            }
        }
    }


    @Composable
    fun VerticalButton() {
        val vm: MainViewModel = koinViewModel()
        val navigator: Navigator = getKoin().get()
        var isOpened by remember { mutableStateOf(false) }
        Box {
            IconButton(modifier = bigIconButtonModifier, onClick = {
                isOpened = true
            }) { Icon(imageVector = Icons.Rounded.MoreVert, null) }
            DropdownMenu(expanded = isOpened, onDismissRequest = { isOpened = !isOpened }) {
                val menuItems = listOf(NavigationDestination.About, NavigationDestination.License)
                menuItems.forEach { item ->
                    val isSelected = item.route == vm.currentRoute.collectAsState().value
                    DropdownMenuItem(modifier = PointerModifier, text = {
                        Row {
                            Icon(imageVector = item.icon!!, contentDescription = item.name, bigIconButtonModifier)
                            Text(
                                item.title,
                                Modifier.padding(start = 4.dp),
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
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
        val vm: MainViewModel = koinViewModel()
        if (!vm.isNotFullScreen().collectAsState().value || vm.isMaterialWindows) {
            IconButton(
                modifier = iconButtonModifier, onClick = vm.closeAppAction()
            ) {
                Icon(Icons.Rounded.Close, null, littleIconModifier)
            }
        }
    }

    @Composable
    fun MaximixedButton() {
        val vm: MainViewModel = koinViewModel()
        if (!vm.isMaterialWindows || !vm.isNotFullScreen().collectAsState().value) return
        val icon = mutableStateOf(Icons.Rounded.Maximize)
        if (!vm.isMaximised.collectAsState().value) {
            icon.value = FontAwesomeIcons.Solid.WindowMaximize
        } else {
            icon.value = FontAwesomeIcons.Solid.WindowRestore
        }
        IconButton(modifier = iconButtonModifier, onClick = { vm.toggleMaximixed() }
        ) { AutoRtlIcon(vm, icon.value, null, modifier = littleIconModifier) }
    }

    @Composable
    fun MinimizedButton() {
        val vm: MainViewModel = koinViewModel()
        if (!vm.isMaterialWindows || !vm.isNotFullScreen().collectAsState().value) return
            IconButton(modifier = iconButtonModifier, onClick = { vm.minimized() }
            ) {
                Icon(Icons.Rounded.Minimize, null, littleIconModifier)
            }
    }

    @Composable
    fun UpdateButton() {
        val vm: MainViewModel = koinViewModel()
        if (vm.showUpdateButton.value) {
            IconButton({ vm.showUpdater() }, modifier = PointerModifier) {
                Icon(Icons.Rounded.SystemUpdateAlt, null)
            }
        }
    }

    @Composable
    fun KofiPostButton(posts: List<data.model.KofiPost>) {
        val vm: MainViewModel = koinViewModel()
        if (!vm.feedSwitch.collectAsState().value) return
        var expanded by remember { mutableStateOf(false) }

        Box {
            // Bouton pour ouvrir/fermer le menu déroulant
            IconButton(
                onClick = {
                    expanded = !expanded
                    if (posts.isEmpty()) {
                        vm.showSnackbar(stringResource("error_not_internet_connection"))
                        vm.kofiPostFetcher()
                    }
                },
                modifier = bigIconButtonModifier
            ) {
                Icon(Icons.Rounded.Campaign, contentDescription = "Localized description", bigIconModifier)
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
                                    Text(
                                        text = post.postTitle,
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Text(
                                        text = post.timeAgo,
                                        textAlign = TextAlign.End,
                                        modifier = Modifier.fillMaxWidth(),
                                        style = MaterialTheme.typography.bodySmall
                                    )
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

