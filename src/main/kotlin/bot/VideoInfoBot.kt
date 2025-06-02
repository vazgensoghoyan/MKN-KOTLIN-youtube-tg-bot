package bot

import bot.commands.helper.CommandFactory
import dev.inmo.tgbotapi.extensions.api.bot.getMe
import dev.inmo.tgbotapi.extensions.api.bot.setMyCommands
import dev.inmo.tgbotapi.extensions.api.telegramBot
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.extensions.behaviour_builder.buildBehaviourWithLongPolling
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onCommand
import dev.inmo.tgbotapi.types.BotCommand

class VideoInfoBot(
    botToken: String,
    private val ytToken: String,
) {
    val bot = telegramBot(botToken)
    val commands = CommandFactory.createAllCommands()

    suspend fun start() {
        bot
            .buildBehaviourWithLongPolling {
                registerCommands()
                setupCommandHandlers()
                println(getMe())
            }.join()
    }

    private suspend fun BehaviourContext.registerCommands() {
        setMyCommands(
            commands.map {
                BotCommand(
                    it.command,
                    it.description,
                )
            },
        )
    }

    private suspend fun BehaviourContext.setupCommandHandlers() {
        commands.forEach { cmd ->
            onCommand(cmd.command, requireOnlyCommandInMessage = true) {
                // commandManager.execute(it.chat.id.chatId.long) {
                cmd.execute(this, it, ytToken)
                // }
            }
        }
    }
}
