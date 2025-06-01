package youtube.models

import kotlinx.serialization.Serializable

@Serializable
data class YtSearchResponse(
    val items: List<YtSearchListItem>,
    val nextPageToken: String? = null,
)

@Serializable
data class YtSearchListItem(
    val id: YtSearchListItemId,
    val snippet: YtSearchListItemSnippet,
    val contentDetails: YtSearchListItemContentDetails? = null,
)

@Serializable
data class YtSearchListItemId(
    val kind: String,
    val playlistId: String? = null,
    val videoId: String? = null,
    val channelId: String? = null,
)

@Serializable
data class YtSearchListItemSnippet(
    val publishedAt: String,
    val channelId: String,
    val title: String,
    val description: String,
    val thumbnails: Map<String, YtSearchListItemThumbnailInfo>? = null,
    val channelTitle: String,
)

@Serializable
data class YtSearchListItemContentDetails(
    val itemCount: Int,
)

@Serializable
data class YtSearchListItemThumbnailInfo(
    val url: String,
    val width: Int? = null,
    val height: Int? = null,
)
