package data.manager

import dorkbox.systemTray.MenuItem
import dorkbox.systemTray.Separator
import dorkbox.systemTray.SystemTray
import org.koin.java.KoinJavaComponent.getKoin
import utils.stringResource
import viewmodel.MainViewModel

class TrayIconManager() {
    val vm: MainViewModel = getKoin().get()
    private val tray = SystemTray.get()
    private val menu = tray.menu
    private var isMenuInitialized = false

    init {
        setupTrayIcon()
        if (!isMenuInitialized) {
            setupMenu()
            isMenuInitialized = true
        }
    }
    private fun setupTrayIcon() {
        val trayIcon = this::class.java.classLoader.getResource("AppIcon.png")
        tray.setImage(trayIcon)
    }

    fun setupMenu() {
        // add a menu item
        menu.add(MenuItem(stringResource("tray_button_open_app")) {
            vm.openTrayButton()
        })


        // add a checkbox
        /*
       menu.add(Checkbox("Checkbox Item") {
           println("Checkbox Item: $it")
       })
         */
        // add a separator
        menu.add(Separator())

        menu.add(MenuItem(stringResource("tray_button_quit_app")) {
            vm.exitAppAction().invoke()
        })

        // add a submenu
        /*
       val submenu = Menu("Submenu")
       submenu.add(MenuItem("sample") {
       })
       menu.add(submenu)

         */
    }
}