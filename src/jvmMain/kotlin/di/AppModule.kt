package di

import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.window.ApplicationScope
import manager.PreferencesManager
import data.repository.ColorRepository
import data.repository.NotesDatabaseRepository
import data.repository.SettingsTabsRepository
import data.repository.TextRepository
import data.repository.ThemeModeRepository
import data.repository.VersionRepository
import data.repository.WindowsPlacementRepository
import moe.tlaster.precompose.navigation.Navigator
import org.koin.dsl.module
import utils.Localization
import viewmodel.MailViewModel
import viewmodel.MainViewModel
import viewmodel.NotesViewModel

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
                applicationScope = applicationScope)
        }
    }

    val notesModule = module {
        single { NotesDatabaseRepository }
        single {
            NotesViewModel(repository = get())
        }
    }

    val contactModule = module {
        single { MailViewModel() }
    }

    val navigatorModule = module {
        single {
            Navigator()
        }
    }

    val snackbarHostState = module {
        single {
            SnackbarHostState()
        }
    }

}