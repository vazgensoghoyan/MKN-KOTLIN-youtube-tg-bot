package youtube

import YT_TOKEN
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class VideoInfoGetterTest {
    private val apiKey = YT_TOKEN // Замените на реальный ключ
    private val testVideoId = "dQw4w9WgXcQ" // Например, видео Rick Astley

    @Test
    fun `should fetch real video info`() =
        runTest {
            val getter = VideoInfoGetter(apiKey, testVideoId)
            val videoInfo = getter.getVideoInfo()

            assertNotNull(videoInfo)
            assertTrue(videoInfo.snippet.title.isNotEmpty(), "Video title should not be empty")
            assertTrue(videoInfo.statistics.viewCount.isNotEmpty(), "View count should not be empty")
            println("Video title: ${videoInfo.snippet.title}")
            println("View count: ${videoInfo.statistics.viewCount}")
        }
}
