package bot

import bot.commands.infoCommand
import bot.commands.searchCommand
import bot.commands.startCommand
import dev.inmo.tgbotapi.bot.ktor.telegramBot
import dev.inmo.tgbotapi.extensions.api.bot.getMe
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.buildBehaviourWithLongPolling
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onCommand

suspend fun videoInfoBot(
    token: String,
    ytToken: String,
) {
    val bot = telegramBot(token)

    try {
        bot
            .buildBehaviourWithLongPolling {
                println(getMe())

                // Welcoming message
                onCommand("start") { command ->
                    try {
                        startCommand(this, command)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        sendTextMessage(
                            chatId = command.chat.id,
                            text = "Error processing start command",
                        )
                    }
                }

                // Searching video
                onCommand("search") { command ->
                    searchCommand(this, command, ytToken)
                }

                // Info about video by its url
                onCommand("info") { command ->
                    infoCommand(this, command, ytToken)
                }
            }.join()
    } catch (e: Exception) {
        println("Bot crashed: ${e.message}")
        e.printStackTrace()
    }
}
