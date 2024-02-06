package data.manager

import dorkbox.systemTray.Checkbox
import dorkbox.systemTray.Menu
import dorkbox.systemTray.MenuItem
import dorkbox.systemTray.Separator
import dorkbox.systemTray.SystemTray
import org.koin.java.KoinJavaComponent.getKoin
import org.koin.mp.KoinPlatform
import viewmodel.MainViewModel
import java.util.logging.LogManager
import kotlin.system.exitProcess

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
        menu.add(MenuItem("Open") {
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

        menu.add(MenuItem("Exit") {
            vm.exit(forceExit = true).invoke()
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