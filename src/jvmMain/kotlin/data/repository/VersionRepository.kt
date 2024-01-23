package data.repository

object VersionRepository {
    fun getCurrentVersion() = System.getProperty("app.version") ?: "Development"

}