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

// Перечисление доступных качеств превью
private enum class ThumbnailQuality(
    val path: String,
) {
    DEFAULT("default.jpg"),
    MEDIUM("mqdefault.jpg"),
    HIGH("hqdefault.jpg"),
    STANDARD("sddefault.jpg"),
    MAX_RES("maxresdefault.jpg"),
}

suspend fun downloadAllThumbnails(videoId: String): List<ByteArray> =
    coroutineScope {
        // Создаем клиент один раз для всех запросов
        val client = HttpClient(CIO)

        // Запускаем все загрузки параллельно
        ThumbnailQuality.entries
            .map { quality ->
                async {
                    try {
                        // Используем IO-диспетчер для сетевых операций
                        withContext(Dispatchers.IO) {
                            val bytes = downloadThumbnail(client, videoId, quality)
                            println("$quality download ended")
                            bytes
                        }
                    } catch (_: IOException) {
                        null // Пропускаем недоступные качества
                    }
                }
            }.awaitAll()
            .filterNotNull() // Фильтруем неудачные загрузки
    }

// "https://i.ytimg.com/vi/$videoId/${quality.path}"
// "https://ytimg.googleusercontent.com/vi/$videoId/hqdefault.jpg"
private suspend fun downloadThumbnail(
    client: HttpClient,
    videoId: String,
    quality: ThumbnailQuality,
): ByteArray = client.get("https://ytimg.googleusercontent.com/vi/$videoId/${quality.path}").readRawBytes()
