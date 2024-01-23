package data.repository

import utils.stringResource

object SettingsTabsRepository {
    fun getTabs(): List<String> {
        return listOf((stringResource("basic_settings")), (stringResource("advanced_settings")))
    }
}