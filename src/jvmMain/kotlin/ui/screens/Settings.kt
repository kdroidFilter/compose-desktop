package ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ui.components.AlignedRow
import ui.components.AlignedRowSpacer
import ui.components.IconToggleButton
import ui.components.PointerModifier
import ui.components.RowLabel
import ui.components.SubTitle
import utils.Localization
import utils.stringResource
import viewmodel.MainViewModel
import kotlin.system.exitProcess


@Composable
fun Settings(vm: MainViewModel) {
    Column(modifier = Modifier.fillMaxSize().padding(start = 32.dp)) {

        val tabs = vm.settingTabs
        val selectedTabIndex = vm.selectedTabIndex.value

        TabRow(
            selectedTabIndex = selectedTabIndex,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = { Text(text = title) },
                    selected = selectedTabIndex == index,
                    onClick = { vm.onTabSelected(index) }
                )
            }
        }

        AnimatedContent(
            targetState = selectedTabIndex,
            transitionSpec = {
                if (targetState > initialState) {
                    (slideInHorizontally(initialOffsetX = { width -> width }) + fadeIn()).togetherWith(
                        slideOutHorizontally(targetOffsetX = { width -> -width }) + fadeOut()
                    )
                } else {
                    (slideInHorizontally(initialOffsetX = { width -> -width }) + fadeIn()).togetherWith(
                        slideOutHorizontally(targetOffsetX = { width -> width }) + fadeOut()
                    )
                }.using(SizeTransform(clip = false))
            }
        ) { targetState ->
            when (targetState) {
                0 -> BasicSettings(vm)
                1 -> {}
            }
        }
    }
}


@Composable
fun BasicSettings(vm: MainViewModel) {
    Box(modifier = Modifier.fillMaxSize().padding(start = 32.dp), Alignment.Center) {
        val state = rememberLazyListState()
        LazyColumn(
            Modifier.fillMaxSize().padding(end = 12.dp),
            state,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                AlignedRowSpacer()
                SubTitle(stringResource("user_interface_behavior_label"))
                Column(Modifier.padding(start = 32.dp)) {
                    WindowsStateRadioButtons(vm)
                    ThemeModeSelection(vm)
                }
                Divider()
            }
            item {
                AlignedRowSpacer()
                SubTitle(stringResource("user_interface_appearance_label"))
                Column(Modifier.padding(start = 32.dp)) {
                    ColorSelection(vm)
                    PaletteSelection(vm)
                    AppBarModeSelection(vm)
                }
                Divider()
            }
            item {
                AlignedRowSpacer()
                SubTitle(stringResource("user_interface_settings_label"))
                Column(Modifier.padding(start = 32.dp)) {
                    KofiButtonSwitch(vm)
                    DarkModeSwitchButton(vm)
                    FeedSwitchButton(vm)
                }
                Divider()
            }
            item {
                AlignedRowSpacer()
                SubTitle(stringResource("language_settings_category_label"))
                LanguageButton(vm)
            }
            item {
                Column(Modifier.padding(start = 32.dp)) {
                    ClearSettingsButton(vm)
                }
            }
        }
        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
            adapter = rememberScrollbarAdapter(
                scrollState = state
            ),
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WindowsStateRadioButtons(vm: MainViewModel) {
    AlignedRow({ RowLabel(stringResource("window_opening_mode_label")) }, {
        var selectedIcon = vm.windowPlacementConfig
        val buttons = vm.getWindowsPlacements()
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            buttons.forEach { button ->
                IconToggleButton(
                    isSelected = selectedIcon == button.config.property,
                    icon = button.icon,
                    onIconSelected = {
                        selectedIcon = button.config.property
                        vm.saveWindowPlacementConfig(button.config.property)
                    },
                    text = button.description,
                )
            }
        }
    })
}

@Composable
fun ClearSettingsButton(vm: MainViewModel) {
    AlignedRowSpacer()
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button({
            vm.clearSettings()
            vm.showSnackbar(
                stringResource("reset_success_message"),
                actionLabel = stringResource("exit_action"),
                action = { exitProcess(0) },
                duration = SnackbarDuration.Long
            )
        }, modifier = PointerModifier) { Text(stringResource("reset_settings_button")) }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ColorSelection(vm: MainViewModel) {
    AlignedRow({ RowLabel(stringResource("select_theme_color_label")) }, {
        FlowRow {
            vm.getColors().forEach { color ->
                // Animation de la taille
                val size = animateDpAsState(
                    targetValue = if (color.name == vm.getCurrentThemeColor()!!.name) 32.dp else 24.dp,
                    animationSpec = tween(
                        durationMillis = 300
                    )
                )
                Box(
                    modifier = PointerModifier.padding(4.dp)
                        .size(size.value) // Utilisation de la valeur animée
                        .clip(RoundedCornerShape(100.dp)).background(color.color).clickable {
                            vm.setThemeColor(color.name)
                        }.align(Alignment.CenterVertically)

                )
            }
        }
    })
}


@Composable
fun PaletteSelection(vm: MainViewModel) {
    AlignedRow({ RowLabel(stringResource("color_palette_label")) }, {
        var isOpened by remember { mutableStateOf(false) }
        var selectedPalette by remember { mutableStateOf(vm.getCurrentThemePalette()!!.name) }

        TextButton(onClick = { isOpened = true }, modifier = PointerModifier) {
            Text(selectedPalette)
        }
        Box {
            DropdownMenu(expanded = isOpened, onDismissRequest = { isOpened = false }) {
                vm.getPalette().forEach { palette ->
                    DropdownMenuItem(text = {
                        Text(
                            text = palette.name,
                            fontWeight = if (palette.name == vm.getCurrentThemePalette()!!.name) FontWeight.Bold else FontWeight.Normal
                        )
                    }, onClick = {
                        vm.setThemePalette(palette.name)
                        selectedPalette = palette.name
                        isOpened = false
                    })
                }
            }
        }
    })
}

@Composable
fun LanguageButton(vm: MainViewModel) {
    AlignedRow({ RowLabel(stringResource("select_language_label")) }, {
        var isOpened by remember { mutableStateOf(false) }
        var selectedLanguage by remember { mutableStateOf(vm.getCurrentLanguageName()) }
        Box {
            TextButton({ isOpened = true }, modifier = PointerModifier) {
                Row {
                    Text(selectedLanguage)
                }
            }
            DropdownMenu(expanded = isOpened, onDismissRequest = { isOpened = !isOpened }) {
                for (language in Localization.availableLanguages()) {
                    DropdownMenuItem(text = {
                        Text(
                            language.name,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                            fontWeight = if (language.code == vm.getCurrentLanguage()) FontWeight.ExtraBold else FontWeight.Normal,
                        )
                    }, onClick = {
                        vm.changeLanguage(language.code)
                        selectedLanguage = language.name
                        isOpened = false
                    })
                }
            }
        }
    })
}

@Composable
fun ThemeModeSelection(vm: MainViewModel) {
    AlignedRow({ RowLabel(stringResource("select_theme_mode_label")) }, {
        var isOpened by remember { mutableStateOf(false) }
        var selectedMode by remember { mutableStateOf(vm.getCurrentTheme().text) }

        Box {
            TextButton({ isOpened = true }, modifier = PointerModifier) {
                Text(selectedMode)
            }
            DropdownMenu(expanded = isOpened, onDismissRequest = { isOpened = !isOpened }) {

                vm.getAllModes().forEach { mode ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = mode.text,
                                fontWeight = if (mode.text == vm.getCurrentTheme().text) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        onClick = {
                            selectedMode = mode.text
                            vm.setTheme(mode.value)
                            isOpened = false
                        },
                    )
                }
            }
        }
    })
}

@Composable
fun AppBarModeSelection(vm: MainViewModel) {
    AlignedRow({ RowLabel(stringResource("select_topappbar_style_label")) }, {
        var isOpened by remember { mutableStateOf(false) }
        var selectedMode by remember { mutableStateOf(vm.getAppBarModeName()) }

        Box {
            TextButton({ isOpened = true }, modifier = PointerModifier) {
                Text(selectedMode)
            }
            DropdownMenu(expanded = isOpened, onDismissRequest = { isOpened = !isOpened }) {

                vm.getAllAppBarMode().forEach { mode ->
                    DropdownMenuItem(text = {
                        Text(
                            text = mode.text,
                            fontWeight = if (mode.text == vm.getAppBarModeName()) FontWeight.Bold else FontWeight.Normal
                        )
                    }, onClick = {
                        selectedMode = mode.text
                        vm.setAppBarMode(mode.name)
                        isOpened = false
                    })
                }
            }
        }
    })
}

@Composable
fun KofiButtonSwitch(vm: MainViewModel) {
    AlignedRow({ RowLabel(stringResource("donation_button_switch_label")) }, {
        val bool = vm.kofiButtonStatus
        Switch(
            checked = bool.value, onCheckedChange = {
                vm.setKofiButtonStatus(it)
            }, modifier = PointerModifier
        )
    })
}

@Composable
fun DarkModeSwitchButton(vm: MainViewModel) {
    AlignedRow({ RowLabel(stringResource("toggle_dark_light_mode_switch_label")) }, {
        val bool = vm.darkModeSwitch
        Switch(
            checked = bool.value, onCheckedChange = {
                vm.setDarkModeSwitch(it)
            }, modifier = PointerModifier
        )
    })
}

@Composable
fun FeedSwitchButton(vm: MainViewModel) {
    AlignedRow({ RowLabel(stringResource("toggle_feed_switch_label")) }, {
        val bool = vm.feedSwitch
        Switch(
            checked = bool.value, onCheckedChange = {
                vm.setFeedSwitch(it)
            }, modifier = PointerModifier
        )
    })
}
