package youtube

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

suspend fun getVideoInfo(
    apiKey: String,
    videoId: String,
): VideoInfo {
    val client = HttpClient(CIO)

    val response =
        client.get("https://www.googleapis.com/youtube/v3/videos") {
            parameter("part", "snippet,statistics")
            parameter("id", videoId)
            parameter("key", apiKey)
        }

    return Json.decodeFromString<YouTubeVideosResponse>(response.bodyAsText()).items.first()
}

@OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class YouTubeVideosResponse(
    val items: List<VideoInfo>,
)

@OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class VideoInfo(
    val id: String,
    val snippet: YtSearchListItemSnippet,
    val statistics: VideoStatistics,
)

@OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class VideoStatistics(
    val viewCount: String,
    val likeCount: String,
    val commentCount: String,
)
