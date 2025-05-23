package youtube

import kotlinx.serialization.Serializable

@Serializable
data class YouTubeSearchResponse(
    val items: List<YouTubeVideo>
)

@Serializable
data class YouTubeVideo(
    val id: String,
    val snippet: YouTubeSnippet,
    val statistics: YouTubeStatistics? = null
)

@Serializable
data class YouTubeSnippet(
    val title: String,
    val channelTitle: String,
    val publishedAt: String
)

@Serializable
data class YouTubeStatistics(
    val viewCount: String
)
