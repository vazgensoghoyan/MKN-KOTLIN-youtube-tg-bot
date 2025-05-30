package youtube
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

suspend fun getPlaylistItems(
    apiKey: String,
    playlistId: String,
    maxResults: Int = 5,
): List<PlaylistItem> {
    val client = HttpClient(CIO)
    val json = Json { ignoreUnknownKeys = true }

    val response =
        client.get("https://www.googleapis.com/youtube/v3/playlistItems") {
            parameter("part", "snippet")
            parameter("playlistId", playlistId)
            parameter("key", apiKey)
            parameter("maxResults", maxResults)
        }

    return json.decodeFromString<PlaylistItemsResponse>(response.bodyAsText()).items
}

@Serializable
data class PlaylistItemsResponse(
    val items: List<PlaylistItem>,
    val nextPageToken: String? = null,
)

@Serializable
data class PlaylistItem(
    val id: String,
    val snippet: PlaylistItemSnippet,
)

@Serializable
data class PlaylistItemSnippet(
    val publishedAt: String,
    val channelTitle: String,
    val channelId: String,
    val title: String,
    val description: String,
    val resourceId: ResourceId,
    val videoOwnerChannelTitle: String,
    val videoOwnerChannelId: String,
)

@Serializable
data class ResourceId(
    val kind: String,
    val videoId: String,
)
