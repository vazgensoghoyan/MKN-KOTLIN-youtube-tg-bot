package bot

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class VideoInfoBotTest {
    private val bot = VideoInfoBot("test_token", "yt_token")

    @Test
    fun shouldRegisterCommands(): Unit =
        runTest {
            assertNotNull(bot.commands)
            assertTrue(bot.commands.isNotEmpty())
        }
}
