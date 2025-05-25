package bot

import bot.commands.infoCommand
import bot.commands.playlistCommand
import bot.commands.searchCommand
import bot.commands.searchConcreteCommand
import bot.commands.startCommand
import bot.commands.thumbnailsCommand
import dev.inmo.tgbotapi.extensions.api.bot.getMe
import dev.inmo.tgbotapi.extensions.api.bot.setMyCommands
import dev.inmo.tgbotapi.extensions.api.telegramBot
import dev.inmo.tgbotapi.extensions.behaviour_builder.buildBehaviourWithLongPolling
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onCommand
import dev.inmo.tgbotapi.types.BotCommand

suspend fun videoInfoBot(
    token: String,
    ytToken: String,
) {
    val bot = telegramBot(token)

    bot
        .buildBehaviourWithLongPolling(
            defaultExceptionsHandler = {
                it.printStackTrace()
            },
        ) {
            // Setting commands for suggestions in bot
            setMyCommands(
                BotCommand(
                    command = "start",
                    description = "Welcoming menu",
                ),
                BotCommand(
                    command = "search",
                    description = "Search on YouTube on request",
                ),
                BotCommand(
                    command = "search_concrete",
                    description = "Search for specific entities (videos, playlists, channels)",
                ),
                BotCommand(
                    command = "info",
                    description = "Information about the video by ID",
                ),
                BotCommand(
                    command = "playlist",
                    description = "Information about the playlist and videos in it by ID",
                ),
                BotCommand(
                    command = "thumbnails",
                    description = "Get all video thumbnails of different qualities",
                ),
            )

            // Welcoming message
            onCommand("start", requireOnlyCommandInMessage = true) {
                startCommand(this, it)
            }

            // Searching in YT
            onCommand("search", requireOnlyCommandInMessage = true) {
                searchCommand(this, it, ytToken)
            }

            // Searching concrete in YT
            onCommand("search_concrete", requireOnlyCommandInMessage = true) {
                searchConcreteCommand(this, it, ytToken)
            }

            // Info about video by ID
            onCommand("info", requireOnlyCommandInMessage = true) {
                infoCommand(this, it, ytToken)
            }

            // Info about playlist by ID
            onCommand("playlist", requireOnlyCommandInMessage = true) {
                playlistCommand(this, it, ytToken)
            }

            // Info about playlist by ID
            onCommand("thumbnails", requireOnlyCommandInMessage = true) {
                thumbnailsCommand(this, it)
            }

            // Printing bot info in console
            println(getMe())
        }.join()
}
