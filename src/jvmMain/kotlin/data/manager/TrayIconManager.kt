package data.manager

import dorkbox.systemTray.MenuItem
import dorkbox.systemTray.Separator
import dorkbox.systemTray.SystemTray
import kotlinx.coroutines.CoroutineScope
import org.koin.java.KoinJavaComponent.getKoin
import utils.stringResource
import viewmodel.MainViewModel

    fun TrayIconManager(context : CoroutineScope) {
    val vm: MainViewModel = getKoin().get()

    SystemTray.DEBUG = true

    val tray = SystemTray.get()

    val menu = tray.menu


    val trayIcon = context::class.java.classLoader.getResourceAsStream("AppIcon.png")
    tray.setImage(trayIcon)


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

