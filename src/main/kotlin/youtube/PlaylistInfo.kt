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
): YtPlaylist {
    val client = HttpClient(CIO)

    val response =
        client.get("https://www.googleapis.com/youtube/v3/playlists") {
            parameter("part", "snippet")
            parameter("id", playlistId)
            parameter("key", apiKey)
        }

    println(response.bodyAsText())

    return Json
        .decodeFromString<YtPlaylistsResponse>(
            response.bodyAsText(),
        ).items
        .first()
}

@OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class YtPlaylistsResponse(
    val items: List<YtPlaylist>,
)

@OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class YtPlaylist(
    val id: String,
    val snippet: YtSearchListItemSnippet,
    val contentDetails: YtSearchListItemContentDetails? = null,
)
