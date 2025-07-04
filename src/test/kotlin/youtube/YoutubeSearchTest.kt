package youtube

import YT_TOKEN
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class YoutubeSearchTest {
    private val apiKey = YT_TOKEN // Замените на реальный ключ API YouTube
    private val testQuery = "Kotlin programming" // Тестовый запрос для поиска

    private val searcher = YoutubeSearch(apiKey)

    @Test
    fun `should fetch real search results`() =
        runTest {
            val searchResults = searcher.youtubeSearch(testQuery, maxResults = 2)

            assertNotNull(searchResults)
            assertTrue(searchResults.isNotEmpty(), "Search results should not be empty")
            println("Fetched search results: ${searchResults.map { it.snippet.title }}")
        }

    @Test
    fun `search results should contain valid items`() =
        runTest {
            val searchResults = searcher.youtubeSearch(testQuery, maxResults = 1)

            val firstItem = searchResults.firstOrNull()
            assertNotNull(firstItem, "First search item should not be null")
            assertTrue(firstItem.snippet.title.isNotEmpty(), "Item title should not be empty")
            println("First result title: ${firstItem.snippet.title}")
        }
}
