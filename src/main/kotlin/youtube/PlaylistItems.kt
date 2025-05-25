package youtube
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

suspend fun getPlaylistItems(
    apiKey: String,
    playlistId: String,
): List<PlaylistItem> {
    val client = HttpClient(CIO)
    return try {
        val response =
            client.get("https://www.googleapis.com/youtube/v3/playlistItems") {
                parameter("part", "snippet")
                parameter("playlistId", playlistId)
                parameter("key", apiKey)
            }

        Json.decodeFromString<PlaylistItemsResponse>(response.bodyAsText()).items
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList()
    } finally {
        client.close()
    }
}

@OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class PlaylistItemsResponse(
    val items: List<PlaylistItem>,
    val nextPageToken: String? = null,
)

@OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class PlaylistItem(
    val id: String,
    val snippet: PlaylistItemSnippet,
)

@OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
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

@OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class ResourceId(
    val kind: String,
    val videoId: String,
)
