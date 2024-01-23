package data.api

import data.model.KofiPost
import io.ktor.client.HttpClient

interface KofiPostScrapper {
    suspend fun fetch(client: HttpClient, _kofiPosts: MutableList<KofiPost>)

}