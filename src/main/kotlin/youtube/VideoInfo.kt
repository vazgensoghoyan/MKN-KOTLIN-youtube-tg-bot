package youtube

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

suspend fun getVideoInfo(
    apiKey: String,
    videoId: String,
): VideoInfo {
    val client = HttpClient(CIO)
    val json = Json { ignoreUnknownKeys = true }

    val response =
        client.get("https://www.googleapis.com/youtube/v3/videos") {
            parameter("part", "snippet,statistics")
            parameter("id", videoId)
            parameter("key", apiKey)
        }

    println(response.bodyAsText())

    return json.decodeFromString<YouTubeVideosResponse>(response.bodyAsText()).items.first()
}

@Serializable
data class YouTubeVideosResponse(
    val items: List<VideoInfo>,
)

@Serializable
data class VideoInfo(
    val id: String,
    val snippet: YtSearchListItemSnippet,
    val statistics: VideoStatistics,
)

@Serializable
data class VideoStatistics(
    val viewCount: String,
    val likeCount: String,
    val commentCount: String,
)
