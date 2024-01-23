package com.example.compose

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.materialkolor.DynamicMaterialTheme
import moe.tlaster.precompose.PreComposeApp
import ui.theme.NotoFont
import viewmodel.MainViewModel

@Composable
fun App(
    vm: MainViewModel,
    content: @Composable() () -> Unit,
) {
    val useDarkTheme = vm.useDarkTheme.value
    val layoutDirection = if (vm.isCurrentLanguageRtl()) LayoutDirection.Rtl else LayoutDirection.Ltr
    vm.registerDarkThemeListener()
    CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
        PreComposeApp {
            MaterialTheme(
                typography = NotoFont(vm).typography
            ) {
                vm.getCurrentThemeColor()?.let { getThemeColor ->
                    vm.getCurrentThemePalette()?.let { getThemePalette ->
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