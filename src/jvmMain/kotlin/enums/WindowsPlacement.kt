package enums

import androidx.compose.ui.window.WindowPlacement

enum class WindowsPlacementConfig(val property: String, val state : WindowPlacement) {
    FLOATING("floating", WindowPlacement.Floating),
    MAXIMIZED("maximized", WindowPlacement.Maximized),
    FULLSCREEN("fullscreen", WindowPlacement.Fullscreen);

    companion object {
        fun getPlacementByProperty(property: String): WindowPlacement? {
            return entries.firstOrNull { it.property.equals(property, ignoreCase = true) }?.state
        }
        fun getPropertyByPlacement(state: WindowPlacement): String? {
            return entries.firstOrNull { it.state == state }?.property
        }
    }
}

