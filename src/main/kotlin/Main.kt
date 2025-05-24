import bot.videoInfoBot
import kotlinx.coroutines.runBlocking

const val BOT_TOKEN = "8057539033:AAEgeoRd-DKmCKz6FUUB5vYu766n4i9ELsk"
const val YT_TOKEN = "AIzaSyDZWPc8ZoH3qGMMCRbHQdZ5DuEByyp-Tfc"

suspend fun main() =
    runBlocking {
        // getVideoInfo(YT_TOKEN, "dQw4w9WgXcQ")

        // searchVideos(YT_TOKEN, "Youtube Api")
        // videoInfo(YT_TOKEN, "dQw4w9WgXcQ")
        videoInfoBot(BOT_TOKEN, YT_TOKEN)
    }
