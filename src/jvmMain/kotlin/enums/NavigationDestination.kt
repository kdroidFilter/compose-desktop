package enums

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Copyright
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Mail
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import utils.stringResource

enum class NavigationDestination(
    val route: String,
    val title: String = stringResource("app_name"),
    val canGoBack: Boolean = true,
    val icon : ImageVector? = null
) {
    FirstConfig("/firstConfig", title = stringResource("first_config_title")),
    Home("/home", canGoBack = false, icon = Icons.Rounded.Home),
    Contact("/contact", title = stringResource("contact_title"), icon = Icons.Rounded.Mail),
    ContactConfirmation("/contactConfirmation", title = stringResource("contact_confirmation_title")),
    Settings("/settings", title = stringResource("settings_title"), icon = Icons.Rounded.Settings),
    About("/about", title = stringResource("about_title"), icon = Icons.Rounded.Info),
    License("/licence", title = stringResource("license_title"), icon = Icons.Rounded.Copyright),
}
