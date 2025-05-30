package bot

import bot.commands.IBotCommand
import bot.commands.SearchCommand
import bot.commands.StartCommand
import dev.inmo.tgbotapi.extensions.api.bot.getMe
import dev.inmo.tgbotapi.extensions.api.bot.setMyCommands
import dev.inmo.tgbotapi.extensions.api.telegramBot
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.extensions.behaviour_builder.buildBehaviourWithLongPolling
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onCommand
import dev.inmo.tgbotapi.types.BotCommand

class VideoInfoBot(
    private val token: String,
    private val ytToken: String,
) {
    val bot = telegramBot(token)
    private val commands = CommandFactory.createAllCommands()
    val commandManager = UserCommandManager()

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
                commandManager.execute(it.chat.id.chatId.long) {
                    cmd.execute(this, it, ytToken)
                }
            }
        }
    }
}

object CommandFactory {
    fun createAllCommands(): List<IBotCommand> =
        listOf(
            StartCommand(),
            SearchCommand(),
            /*SearchConcreteCommand(),
            InfoCommand(),
            PlaylistCommand(),
            ThumbnailCommand(),
            ThumbnailsCommand(),*/
        )
}

enum class VideoInfoBotCommands(
    val command: String,
    val description: String,
) {
    THUMBNAIL("thumbnail", "Get all video thumbnails of different qualities"),
    THUMBNAILS("thumbnails", "Get the best thumbnails for given video IDs"),
}
