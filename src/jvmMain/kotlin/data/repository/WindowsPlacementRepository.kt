package data.repository

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Fullscreen
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.WindowMaximize
import compose.icons.fontawesomeicons.solid.WindowRestore
import data.model.WindowsPlacementModel
import enums.WindowsPlacementConfig
import utils.stringResource

object WindowsPlacementRepository {
    fun getWindowsPlacements() : List<WindowsPlacementModel> {
        return listOf(
            WindowsPlacementModel(
                stringResource("window_placement_floating_label"),
                FontAwesomeIcons.Solid.WindowRestore,
                WindowsPlacementConfig.FLOATING
            ),
            WindowsPlacementModel(
                stringResource("window_placement_maximized_label"),
                FontAwesomeIcons.Solid.WindowMaximize,
                WindowsPlacementConfig.MAXIMIZED
            ),
            WindowsPlacementModel(
                stringResource("window_placement_fullscreen_label"),
                Icons.Rounded.Fullscreen,
                WindowsPlacementConfig.FULLSCREEN
            ),
        )
    }

}