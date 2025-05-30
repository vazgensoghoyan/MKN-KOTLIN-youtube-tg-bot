package youtube

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

suspend fun getPlaylistInfo(
    apiKey: String,
    playlistId: String,
): YtPlaylist {
    val client = HttpClient(CIO)
    val json = Json { ignoreUnknownKeys = true }

    val response =
        client.get("https://www.googleapis.com/youtube/v3/playlists") {
            parameter("part", "snippet")
            parameter("id", playlistId)
            parameter("key", apiKey)
        }

    return json.decodeFromString<YtPlaylistsResponse>(response.bodyAsText()).items.first()
}

@Serializable
data class YtPlaylistsResponse(
    val items: List<YtPlaylist>,
)

@Serializable
data class YtPlaylist(
    val id: String,
    val snippet: YtSearchListItemSnippet,
    val contentDetails: YtSearchListItemContentDetails? = null,
)
