import bot.photoBot
import dev.inmo.tgbotapi.bot.ktor.telegramBot
import dev.inmo.tgbotapi.extensions.api.bot.getMe

const val TOKEN = "8057539033:AAEgeoRd-DKmCKz6FUUB5vYu766n4i9ELsk"

suspend fun main() {
    val bot = telegramBot(TOKEN)
    println(bot.getMe())
}