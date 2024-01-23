package data.model

import androidx.compose.ui.graphics.Color
import com.materialkolor.PaletteStyle

data class ColorThemeModel(
    val name : String,
    val color : Color
)

data class PaletteThemeMode(
    val name : String,
    val palette : PaletteStyle
)