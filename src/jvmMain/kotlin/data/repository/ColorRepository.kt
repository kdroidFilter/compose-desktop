package data.repository

import androidx.compose.ui.graphics.Color
import com.materialkolor.PaletteStyle
import data.model.ColorThemeModel
import data.model.PaletteThemeMode

object ColorRepository {
    fun getColors(): List<ColorThemeModel> {
        return listOf(
            ColorThemeModel("Vivid Red", Color(0xFFD32F2F)),
            ColorThemeModel("Deep Fuchsia", Color(0xFFC2185B)),
            ColorThemeModel("Mystic Violet", Color(0xFF7B1FA2)),
            ColorThemeModel("Dark Indigo", Color(0xFF512DA8)),
            ColorThemeModel("Royal Blue", Color(0xFF303F9F)),
            ColorThemeModel("Bright Sky Blue", Color(0xFF1976D2)),
            ColorThemeModel("Luminous Turquoise", Color(0xFF0288D1)),
            ColorThemeModel("Ocean Cyan", Color(0xFF0097A7)),
            ColorThemeModel("Emerald Green", Color(0xFF00796B)),
            ColorThemeModel("Forest Green", Color(0xFF388E3C)),
            ColorThemeModel("Olive Green", Color(0xFF689F38)),
            ColorThemeModel("Chartreuse Yellow", Color(0xFFAFB42B)),
            ColorThemeModel("Sun Yellow", Color(0xFFFBC02D)),
            ColorThemeModel("Tangerine Orange", Color(0xFFFFA000)),
            ColorThemeModel("Burnt Orange", Color(0xFFF57C00)),
            ColorThemeModel("Brick Red", Color(0xFFE64A19)),
            ColorThemeModel("Cocoa Brown", Color(0xFF5D4037)),
            ColorThemeModel("Slate Grey", Color(0xFF616161)),
            ColorThemeModel("Steel Blue", Color(0xFF455A64)),
            ColorThemeModel("Midnight Blue", Color(0xFF263238))
        )
    }
    fun getPalette() : List<PaletteThemeMode> {
        return listOf(
            PaletteThemeMode("Vibrant", PaletteStyle.Vibrant),
            PaletteThemeMode("Monochrome", PaletteStyle.Monochrome),
            PaletteThemeMode("Content", PaletteStyle.Content),
            PaletteThemeMode("Expressive", PaletteStyle.Expressive),
            PaletteThemeMode("Fidelity", PaletteStyle.Fidelity),
            PaletteThemeMode("Fruit Salad", PaletteStyle.FruitSalad),
            PaletteThemeMode("Neutral", PaletteStyle.Neutral),
            PaletteThemeMode("Rainbow", PaletteStyle.Rainbow),
            PaletteThemeMode("Tonal Spot", PaletteStyle.TonalSpot)
        )
    }
}