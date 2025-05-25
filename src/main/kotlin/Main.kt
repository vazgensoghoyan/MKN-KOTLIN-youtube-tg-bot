import bot.videoInfoBot

const val BOT_TOKEN = "8057539033:AAEgeoRd-DKmCKz6FUUB5vYu766n4i9ELsk"
const val YT_TOKEN = "AIzaSyDZWPc8ZoH3qGMMCRbHQdZ5DuEByyp-Tfc"

suspend fun main() {
    // getPlaylistItems(YT_TOKEN, "PLB03EA9545DD188C3")
    // searchVideos(YT_TOKEN, "QkakBNFfirA")
    // getPlaylistItems(YT_TOKEN, "OLAK5uy_nt3pLOZAhYh_YZJysvDSPgJc-Xa9HIg0U")

    videoInfoBot(BOT_TOKEN, YT_TOKEN)
}
