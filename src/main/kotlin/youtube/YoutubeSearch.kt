package youtube

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.json.Json

class YoutubeSearch(
    private val apiToken: String,
    private val whatToSearch: String = "video,playlist,channel",
) {
    private val client = HttpClient(CIO)
    val json = Json { ignoreUnknownKeys = true }

    suspend fun youtubeSearch(
        query: String,
        maxResults: Int = 5,
        maxPages: Int = 1,
    ): List<YtSearchListItem> =
        coroutineScope {
            val allPlaylists = mutableListOf<YtSearchListItem>()
            var pageToken: String? = null
            var pagesFetched = 0

            while (pagesFetched < maxPages && (pageToken != null || pagesFetched == 0)) {
                val response = fetchYoutubePage(query, pageToken, maxResults)

                allPlaylists.addAll(response.items)
                pageToken = response.nextPageToken

                pagesFetched++
            }

            allPlaylists
        }

    private suspend fun fetchYoutubePage(
        query: String,
        pageToken: String? = null,
        maxResults: Int = 5,
    ): YtSearchResponse {
        val response =
            client.get("https://www.googleapis.com/youtube/v3/search") {
                parameter("part", "snippet")
                parameter("q", query)
                parameter("maxResults", maxResults)
                parameter("key", apiToken)
                parameter("type", whatToSearch)
                pageToken?.let { parameter("pageToken", it) }
            }

        return json.decodeFromString(response.bodyAsText())
    }
}
