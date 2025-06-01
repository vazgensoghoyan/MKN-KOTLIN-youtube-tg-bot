package youtube

import kotlinx.serialization.Serializable

@Serializable
data class YtPlaylistsResponse(
    val items: List<YtPlaylist>,
)

@Serializable
data class YtPlaylist(
    val id: String,
    val snippet: YtSearchListItemSnippet,
    val contentDetails: YtSearchListItemContentDetails? = null,
)
