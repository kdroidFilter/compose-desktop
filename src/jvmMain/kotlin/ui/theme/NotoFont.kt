package ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.platform.Font
import viewmodel.MainViewModel

class NotoFont(val vm : MainViewModel){
    private val customFontFamily = if(vm.isCurrentLanguageRtl()) FontFamily( Font("font/notosanshebrew.ttf"))  else FontFamily( Font("font/notosans.ttf"))
    private val defaultTypography = Typography()

    val typography = Typography(
        displayLarge = defaultTypography.displayLarge.copy(fontFamily = customFontFamily),
        displayMedium = defaultTypography.displayMedium.copy(fontFamily = customFontFamily),
        displaySmall = defaultTypography.displaySmall.copy(fontFamily = customFontFamily),
        headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = customFontFamily),
        headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = customFontFamily),
        headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = customFontFamily),
        titleLarge = defaultTypography.titleLarge.copy(fontFamily = customFontFamily),
        titleMedium = defaultTypography.titleMedium.copy(fontFamily = customFontFamily),
        titleSmall = defaultTypography.titleSmall.copy(fontFamily = customFontFamily),
        bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = customFontFamily),
        bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = customFontFamily),
        bodySmall = defaultTypography.bodySmall.copy(fontFamily = customFontFamily),
        labelLarge = defaultTypography.labelLarge.copy(fontFamily = customFontFamily),
        labelMedium = defaultTypography.labelMedium.copy(fontFamily = customFontFamily),
        labelSmall = defaultTypography.labelSmall.copy(fontFamily = customFontFamily)
    )
}