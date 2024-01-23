package data.api

import androidx.compose.runtime.MutableState
import io.ktor.client.HttpClient

interface TextScrapper {
    suspend fun fetch(client: HttpClient, url: String, content : MutableState<String>)
}