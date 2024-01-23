package data.model

import androidx.compose.ui.graphics.vector.ImageVector
import enums.WindowsPlacementConfig

data class WindowsPlacementModel(
    val description: String,
    val icon: ImageVector,
    val config: WindowsPlacementConfig
    )