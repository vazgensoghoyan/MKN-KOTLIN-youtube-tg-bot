package youtube

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json

class VideoInfoGetter(
    private val apiKey: String,
    private val videoId: String,
) {
    private val client: HttpClient = HttpClient(CIO)
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun getVideoInfo(): VideoInfo {
        val response =
            client.get("https://www.googleapis.com/youtube/v3/videos") {
                parameter("part", "snippet,statistics")
                parameter("id", videoId)
                parameter("key", apiKey)
            }

        println(response.bodyAsText())

        return json.decodeFromString<YouTubeVideosResponse>(response.bodyAsText()).items.first()
    }
}
