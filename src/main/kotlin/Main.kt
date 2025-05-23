import bot.videoInfoBot

const val BOT_TOKEN = "8057539033:AAEgeoRd-DKmCKz6FUUB5vYu766n4i9ELsk"
const val YT_TOKEN = "AIzaSyDZWPc8ZoH3qGMMCRbHQdZ5DuEByyp-Tfc"

suspend fun main() {
    // getVideoInfo(YT_TOKEN, "dQw4w9WgXcQ")

    videoInfoBot(BOT_TOKEN, YT_TOKEN)
}
