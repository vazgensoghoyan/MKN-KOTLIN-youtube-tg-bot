package youtube

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

class YoutubeSearch(
    private val apiToken: String,
    private val whatToSearch: String? = "video,playlist,channel",
) {
    private val client = HttpClient(CIO)

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
                pageToken?.let { parameter("pageToken", it) }
                whatToSearch?.let { parameter("type", whatToSearch) }
            }

        return Json.decodeFromString(response.bodyAsText())
    }
}

@OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class YtSearchResponse(
    val items: List<YtSearchListItem>,
    val nextPageToken: String? = null,
)

@OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class YtSearchListItem(
    val id: YtSearchListItemId,
    val snippet: YtSearchListItemSnippet,
    val contentDetails: YtSearchListItemContentDetails? = null,
)

@OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class YtSearchListItemId(
    val kind: String,
    val playlistId: String? = null,
    val videoId: String? = null,
    val channelId: String? = null,
)

@OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class YtSearchListItemSnippet(
    val publishedAt: String,
    val channelId: String,
    val title: String,
    val description: String,
    val thumbnails: Map<String, YtSearchListItemThumbnailInfo>? = null,
    val channelTitle: String,
)

@OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class YtSearchListItemContentDetails(
    val itemCount: Int,
)

@OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class YtSearchListItemThumbnailInfo(
    val url: String,
    val width: Int? = null,
    val height: Int? = null,
)
