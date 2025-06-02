package youtube

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ThumbnailDownloaderTest {
    @Test
    fun `should download real YouTube thumbnail`() =
        runTest {
            val downloader = ThumbnailDownloader() // Использует реальный HttpClient

            val result =
                downloader.downloadThumbnail(
                    videoId = "dQw4w9WgXcQ", // Например, видео Rick Astley
                    quality = ThumbnailDownloader.ThumbnailQuality.HIGH,
                )

            assertNotNull(result, "Thumbnail should not be null")
            assertTrue(result.isNotEmpty(), "Thumbnail data should not be empty")
        }
}
