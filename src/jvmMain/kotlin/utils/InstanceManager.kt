package utils

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.mp.KoinPlatform.getKoin
import viewmodel.MainViewModel
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket
import kotlin.system.exitProcess

object InstanceManager {
    private const val port = Config.PORT
    fun checkForExistingInstance() {
        try {
            // Tente de se connecter au serveur socket existant
            Socket("localhost", port).use { socket ->
                println("Une instance est déjà en cours d'exécution. Envoi d'une demande pour exécuter une fonction spécifique.")
                PrintWriter(socket.getOutputStream(), true).println("startFunction")
                // Termine la nouvelle instance après avoir envoyé le message
                exitProcess(0)
            }
        } catch (e: Exception) {
            println("Aucune instance en cours d'exécution détectée. Démarrage de l'instance principale.")
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun showActiveInstanceWindow() {
        val vm : MainViewModel = getKoin().get()
        GlobalScope.launch(Dispatchers.IO) {
            try {
                ServerSocket(port).use { serverSocket ->
                    println("En écoute sur le port $port...")
                    while (true) {
                        val clientSocket = serverSocket.accept()
                        println("Connexion client acceptée.")
                        val line = clientSocket.getInputStream().bufferedReader().readLine()
                        if (line == "startFunction") {
                            withContext(Dispatchers.Main) {
                                vm.openTrayButton()
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}