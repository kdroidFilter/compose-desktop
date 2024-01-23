package data.implementation

import androidx.compose.runtime.MutableState
import data.api.TextScrapper
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText

class TextScrapperImpl : TextScrapper {
    override suspend fun fetch(client: HttpClient, url: String, content: MutableState<String>) {
        try {
            val response = client.get(url).bodyAsText()
            content.value = response
        } catch (e: Exception) {
            println("Une erreur s'est produite: ${e.message}")
        }
    }
}