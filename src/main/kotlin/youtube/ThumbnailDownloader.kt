package youtube

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.statement.readRawBytes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import java.io.IOException

class ThumbnailDownloader(
    private val client: HttpClient = HttpClient(CIO),
) {
    suspend fun downloadThumbnailsForVideos(
        apiKey: String,
        videoIds: List<String>,
    ): List<ByteArray> =
        coroutineScope {
            videoIds
                .map { videoId ->
                    async {
                        try {
                            val info =
                                async {
                                    val infoGetter = VideoInfoGetter(apiKey, videoId)
                                    infoGetter
                                        .getVideoInfo()
                                        .snippet.thumbnails!!
                                        .size
                                }
                            val bytes = async { downloadAllThumbnailVariants(videoId) }

                            bytes.await()[info.await() - 1]
                        } catch (_: Exception) {
                            null
                        }
                    }
                }.awaitAll()
                .filterNotNull()
        }

    suspend fun downloadAllThumbnailVariants(videoId: String): List<ByteArray> =
        coroutineScope {
            // Running all downloads in parallel
            ThumbnailQuality.entries
                .map { quality ->
                    async {
                        try {
                            // We use an IO manager for network operations
                            withContext(Dispatchers.IO) {
                                downloadThumbnail(videoId, quality)
                            }
                        } catch (_: IOException) {
                            null // Missing unavailable qualities
                        }
                    }
                }.awaitAll()
                .filterNotNull() // Filtering failed downloads
        }

    // "https://i.ytimg.com/vi/$videoId/${quality.path}"
    // "https://ytimg.googleusercontent.com/vi/$videoId/${quality.path}"
    suspend fun downloadThumbnail(
        videoId: String,
        quality: ThumbnailQuality,
    ): ByteArray = client.get("https://ytimg.googleusercontent.com/vi/$videoId/${quality.path}").readRawBytes()

    // Listing the available preview qualities
    enum class ThumbnailQuality(
        val path: String,
    ) {
        DEFAULT("default.jpg"),
        MEDIUM("mqdefault.jpg"),
        HIGH("hqdefault.jpg"),
        STANDARD("sddefault.jpg"),
        MAX_RES("maxresdefault.jpg"),
    }
}
