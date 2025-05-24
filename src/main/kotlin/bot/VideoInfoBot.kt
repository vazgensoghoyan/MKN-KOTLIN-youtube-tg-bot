package bot

import bot.commands.infoCommand
import bot.commands.searchCommand
import bot.commands.startCommand
import dev.inmo.tgbotapi.bot.ktor.telegramBot
import dev.inmo.tgbotapi.extensions.api.bot.getMe
import dev.inmo.tgbotapi.extensions.behaviour_builder.buildBehaviourWithLongPolling
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onCommand

suspend fun videoInfoBot(
    token: String,
    ytToken: String,
) {
    val bot = telegramBot(token)

    bot
        .buildBehaviourWithLongPolling {
            println(getMe())

            // Welcoming message
            onCommand("start", requireOnlyCommandInMessage = true) {
                startCommand(this, it)
            }

            // Searching video
            onCommand("search", requireOnlyCommandInMessage = false) {
                searchCommand(this, it, ytToken)
            }

            // Info about video by its url
            onCommand("info", requireOnlyCommandInMessage = false) {
                infoCommand(this, it, ytToken)
            }
        }.join()

    try {
    } catch (e: Exception) {
        println("Bot crashed: ${e.message}")
        e.printStackTrace()
    }
}
