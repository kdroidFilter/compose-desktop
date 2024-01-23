package data.implementation

import com.google.gson.Gson
import data.api.VersionApi
import data.model.AppVersion
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText

class VersionApiImpl(private val client: HttpClient) : VersionApi {
    override suspend fun getCurrentVersion(): AppVersion {
        return try {
            val response: HttpResponse =
                client.get("https://raw.githubusercontent.com/kdroidFilter/Compose-Multiplatform-Template/master/updaterApi.json")
            val responseBody = response.bodyAsText()
            Gson().fromJson(responseBody, AppVersion::class.java)
        } catch (e: Exception) {
            println("Erreur: ${e.message}")
            AppVersion("N/A", "N/A", "")
        }
    }
}
