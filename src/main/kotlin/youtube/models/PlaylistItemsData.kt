package youtube.models

import kotlinx.serialization.Serializable

@Serializable
data class PlaylistItemsResponse(
    val items: List<PlaylistItem>,
    val nextPageToken: String? = null,
)

@Serializable
data class PlaylistItem(
    val id: String,
    val snippet: PlaylistItemSnippet,
)

@Serializable
data class PlaylistItemSnippet(
    val publishedAt: String,
    val channelTitle: String,
    val channelId: String,
    val title: String,
    val description: String,
    val resourceId: ResourceId,
    val videoOwnerChannelTitle: String,
    val videoOwnerChannelId: String,
)

@Serializable
data class ResourceId(
    val kind: String,
    val videoId: String,
)
