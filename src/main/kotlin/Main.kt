import bot.VideoInfoBot

const val BOT_TOKEN = "7785348052:AAGC8R29NsNmH45QzkyazTaXE2BVZkKvq48"
const val YT_TOKEN = "AIzaSyDZWPc8ZoH3qGMMCRbHQdZ5DuEByyp-Tfc"

suspend fun main() {
    val bot = VideoInfoBot(BOT_TOKEN, YT_TOKEN)
    bot.start()
}
