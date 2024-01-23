package data.implementation

import data.api.KofiPostScrapper
import data.model.KofiPost
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

class KofiPostScrapperImpl : KofiPostScrapper {
    override suspend fun fetch(client: HttpClient, _kofiPosts: MutableList<KofiPost>) {
        withContext(Dispatchers.IO) {
            try {
                val response =
                    client.get("https://ko-fi.com/Buttons/LoadRecentPosts?buttonId=J3J6N3QW7")
                        .bodyAsText()
                val document = Jsoup.parse(response, "UTF-8")
                val feedItems = document.select(".row")

                val newPosts = feedItems.mapNotNull { item ->
                    val timeAgo = item.select(".feeditem-time").text()
                    val postTitle = item.select(".recent-posts-title span").text()
                    val style = item.select(".kfds-srf-empty-state-post").first()?.attr("style")

                    // Extraction de l'URL de l'image
                    val imageUrl = style?.substringAfter("url(")?.substringBeforeLast(")")
                        ?.removeSurrounding("'") ?: ""

                    val postUrl = item.select("a").first()?.attr("href") ?: ""

                    if (postTitle.isNotEmpty() && postUrl.isNotEmpty()) {
                        KofiPost(timeAgo, postTitle, imageUrl, postUrl)
                    } else null
                }

                _kofiPosts.clear()
                _kofiPosts.addAll(newPosts)

            } catch (e: Exception) {
                println("Erreur réseau : $e")
            }
        }
    }
}