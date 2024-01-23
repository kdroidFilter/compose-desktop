package data.repository

object VersionRepository {
    fun getCurrentVersion() = System.getProperty("jpackage.app-version") ?: "debug"

}