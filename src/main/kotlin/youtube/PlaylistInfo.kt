package youtube

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

suspend fun getPlaylistInfo(
    apiKey: String,
    playlistId: String,
): Playlist? {
    val client = HttpClient(CIO)
    return try {
        val response =
            client.get("https://www.googleapis.com/youtube/v3/playlists") {
                parameter("part", "snippet,contentDetails")
                parameter("id", playlistId)
                parameter("key", apiKey)
            }

        println(response.bodyAsText())

        Json.decodeFromString<PlaylistResponse>(response.bodyAsText()).items.firstOrNull()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    } finally {
        client.close()
    }
}

@OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class PlaylistResponse(
    val items: List<Playlist>,
)

@OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class Playlist(
    val id: String,
    val snippet: PlaylistSnippet,
    val contentDetails: PlaylistContentDetails?,
)

@OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class PlaylistSnippet(
    val publishedAt: String,
    val channelId: String,
    val title: String,
    val description: String,
    val channelTitle: String,
)

@OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class PlaylistContentDetails(
    val itemCount: Int,
)
