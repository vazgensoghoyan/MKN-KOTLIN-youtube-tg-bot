@file:Suppress("ktlint:standard:no-wildcard-imports")

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@Serializable
@JsonIgnoreUnknownKeys
data class YouTubeVideoResponse(
    val kind: String,
    val etag: String,
    val items: List<YouTubeVideo>,
    val pageInfo: PageInfo,
)

@Serializable
@JsonIgnoreUnknownKeys
data class PageInfo(
    val totalResults: Int,
    val resultsPerPage: Int,
)

@Serializable
@JsonIgnoreUnknownKeys
data class YouTubeVideo(
    val kind: String,
    val etag: String,
    val id: String,
    val snippet: VideoSnippet,
    val contentDetails: VideoContentDetails,
    val statistics: VideoStatistics? = null,
    val status: VideoStatus? = null, // Теперь nullable
)

@Serializable
@JsonIgnoreUnknownKeys
data class VideoSnippet(
    val publishedAt: String,
    val channelId: String,
    val title: String,
    val description: String,
    val channelTitle: String,
    val tags: List<String>? = null,
    val categoryId: String? = null,
)

@Serializable
@JsonIgnoreUnknownKeys
data class VideoContentDetails(
    val duration: String,
    val dimension: String? = null,
    val definition: String? = null,
)

@Serializable
@JsonIgnoreUnknownKeys
data class VideoStatistics(
    val viewCount: String? = null,
    val likeCount: String? = null,
    val commentCount: String? = null,
)

@Serializable
@JsonIgnoreUnknownKeys
data class VideoStatus(
    val uploadStatus: String? = null,
    val privacyStatus: String? = null,
)

class YouTubeApiKtor(
    private val apiKey: String,
) {
    private val client =
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        isLenient = true
                    },
                )
            }
        }

    suspend fun getVideoInfo(videoId: String): YouTubeVideo? =
        try {
            val response =
                client.get("https://www.googleapis.com/youtube/v3/videos") {
                    url {
                        parameters.append("part", "snippet,contentDetails,statistics")
                        parameters.append("id", videoId)
                        parameters.append("key", apiKey)
                    }
                }

            val responseBody: YouTubeVideoResponse = response.body()
            responseBody.items.firstOrNull()
        } catch (e: Exception) {
            println("Error fetching video info: ${e.message}")
            null
        }

    suspend fun getVideoInfoSimple(videoId: String): YouTubeVideo? =
        try {
            val responseJson =
                client
                    .get("https://www.googleapis.com/youtube/v3/videos") {
                        parameter("part", "snippet,contentDetails,statistics")
                        parameter("id", videoId)
                        parameter("key", apiKey)
                    }.body<String>()

            Json.decodeFromString<YouTubeVideoResponse>(responseJson).items.firstOrNull()
        } catch (e: Exception) {
            println("Error: ${e.message}")
            null
        }

    fun getVideoInfo(video: YouTubeVideo): String =
        """=== Video Information ===
        Title: ${video.snippet.title}
        Channel: ${video.snippet.channelTitle}
        Published: ${video.snippet.publishedAt}
        Duration: ${video.contentDetails.duration}
        Views: ${video.statistics?.viewCount ?: "N/A"}
        Likes: ${video.statistics?.likeCount ?: "N/A"}
        Comments: ${video.statistics?.commentCount ?: "N/A"}
        Description (first 100 chars):\n ${video.snippet.description.take(100)}..."""

    suspend fun close() {
        client.close()
    }
}

suspend fun getVideoInfo(
    apiKey: String,
    videoId: String,
): String {
    val youTubeApi = YouTubeApiKtor(apiKey)
    var ans: String = ""

    try {
        val videoInfo = youTubeApi.getVideoInfoSimple(videoId)
        if (videoInfo != null) {
            ans = youTubeApi.getVideoInfo(videoInfo)
        }
    } finally {
        youTubeApi.close()
    }

    if (ans == "") {
        return "Video not found or error occurred"
    }

    return ans
}
