package youtube

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json

class PlaylistInfoGetter(
    private val apiKey: String,
    private val playlistId: String,
) {
    private val client: HttpClient = HttpClient(CIO)
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun getPlaylistItems(maxResults: Int = 5): List<PlaylistItem> {
        val response =
            client.get("https://www.googleapis.com/youtube/v3/playlistItems") {
                parameter("part", "snippet")
                parameter("playlistId", playlistId)
                parameter("key", apiKey)
                parameter("maxResults", maxResults)
            }

        return json.decodeFromString<PlaylistItemsResponse>(response.bodyAsText()).items
    }

    suspend fun getPlaylistInfo(): YtPlaylist {
        val response =
            client.get("https://www.googleapis.com/youtube/v3/playlists") {
                parameter("part", "snippet")
                parameter("id", playlistId)
                parameter("key", apiKey)
            }

        return json.decodeFromString<YtPlaylistsResponse>(response.bodyAsText()).items.first()
    }
}
