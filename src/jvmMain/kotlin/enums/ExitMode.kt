package enums

import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.WindowScope
import kotlin.system.exitProcess

enum class ExitMode(val text : String) {
    EXIT("exit"),
    BACKGROUND("background")
}