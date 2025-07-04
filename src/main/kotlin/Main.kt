import bot.VideoInfoBot

const val BOT_TOKEN = ""
const val YT_TOKEN = ""

suspend fun main() {
    val bot = VideoInfoBot(BOT_TOKEN, YT_TOKEN)
    bot.start()
}
