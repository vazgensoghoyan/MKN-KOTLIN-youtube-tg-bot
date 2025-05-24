package youtube

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class YouTubeSearchResponse(
    val items: List<YoutubeVideo>,
)

@OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class YoutubeVideo(
    val id: VideoId,
    val snippet: SearchSnippet,
)

@OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class VideoId(
    val videoId: String,
)

@OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class SearchSnippet(
    val publishedAt: String,
    val channelId: String,
    val title: String,
    val description: String,
    val channelTitle: String,
)

suspend fun searchVideos(
    apiKey: String,
    query: String,
): List<YoutubeVideo> {
    val client = HttpClient(CIO)
    return try {
        val response =
            client.get("https://www.googleapis.com/youtube/v3/search") {
                parameter("part", "snippet")
                parameter("q", query)
                parameter("key", apiKey)
                parameter("type", "video")
            }

        Json.decodeFromString<YouTubeSearchResponse>(response.bodyAsText()).items
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList()
    } finally {
        client.close()
    }
}
