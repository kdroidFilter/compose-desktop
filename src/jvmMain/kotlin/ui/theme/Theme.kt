package com.example.compose

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.materialkolor.DynamicMaterialTheme
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.koin.koinViewModel
import ui.theme.NotoFont
import viewmodel.MainViewModel

@Composable
fun App(
    content: @Composable() () -> Unit,
) {
    val vm: MainViewModel = koinViewModel()
    val useDarkTheme = vm.useDarkTheme.collectAsState().value
    val layoutDirection = if (vm.isCurrentLanguageRtl()) LayoutDirection.Rtl else LayoutDirection.Ltr
    vm.registerDarkThemeListener()
    CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
        PreComposeApp {
            MaterialTheme(
                typography = NotoFont(vm).typography
            ) {
                vm.currentThemeColor.collectAsState().value?.let { getThemeColor ->
                    vm.currentThemePalette.collectAsState().value?.let { getThemePalette ->
                        DynamicMaterialTheme(
                            seedColor = getThemeColor.color,
                            style = getThemePalette.palette,
                            useDarkTheme = useDarkTheme,
                            content = content
                        )
                    }
                }
            }
        }
    }
}