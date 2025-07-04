package youtube

import YT_TOKEN
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class PlaylistInfoGetterTest {
    private val apiKey = YT_TOKEN // Замените на реальный ключ
    private val testPlaylistId = "PLMC9KNkIncKtPzgY-5rmhvj7fax8fdxoj" // Например, плейлист с музыкой

    @Test
    fun `should fetch real playlist items`() =
        runTest {
            val getter = PlaylistInfoGetter(apiKey, testPlaylistId)
            val items = getter.getPlaylistItems(maxResults = 2)

            assertNotNull(items)
            assertTrue(items.isNotEmpty(), "Playlist items should not be empty")
            println("Fetched items: ${items.map { it.snippet.title }}")
        }

    @Test
    fun `should fetch real playlist info`() =
        runTest {
            val getter = PlaylistInfoGetter(apiKey, testPlaylistId)
            val playlist = getter.getPlaylistInfo()

            assertNotNull(playlist)
            assertTrue(playlist.snippet.title.isNotEmpty(), "Playlist title should not be empty")
            println("Playlist title: ${playlist.snippet.title}")
        }
}
