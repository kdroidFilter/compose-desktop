package di

import androidx.compose.ui.window.ApplicationScope
import data.manager.PreferencesManager
import data.repository.ColorRepository
import data.repository.SettingsTabsRepository
import data.repository.TextRepository
import data.repository.ThemeModeRepository
import data.repository.VersionRepository
import data.repository.WindowsPlacementRepository
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.stateholder.SavedStateHolder
import org.koin.dsl.module
import utils.Localization
import viewmodel.MainViewModel

object AppModule {
    val appModule = module {
        // Définir les dépendances en tant que singletons
        single { Localization }
        single { PreferencesManager }
        single { ColorRepository }
        single { VersionRepository }
        single { WindowsPlacementRepository }
        single { ThemeModeRepository }
        single { TextRepository }
        single { SettingsTabsRepository }

        // Définir MainViewModel
        single { (applicationScope: ApplicationScope) ->
            MainViewModel(
                localizationRepository = get(),
                preferencesManager = get(),
                colorRepository = get(),
                versionReposition = get(),
                windowsPlacementRepository = get(),
                themeModeRepository = get(),
                textRepository = get(),
                settingsTabsRepository = get(),
                applicationScope = applicationScope
            )
        }
    }

    val navigatorModule = module {
        single {
            Navigator()
        }
    }
}