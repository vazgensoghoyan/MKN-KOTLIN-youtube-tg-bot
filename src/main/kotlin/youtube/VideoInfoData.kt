package youtube

import kotlinx.serialization.Serializable

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
