package bot

import bot.commands.infoCommand
import bot.commands.playlistCommand
import bot.commands.searchCommand
import bot.commands.startCommand
import dev.inmo.tgbotapi.extensions.api.bot.getMe
import dev.inmo.tgbotapi.extensions.api.bot.setMyCommands
import dev.inmo.tgbotapi.extensions.api.send.sendMessage
import dev.inmo.tgbotapi.extensions.api.telegramBot
import dev.inmo.tgbotapi.extensions.behaviour_builder.buildBehaviourWithLongPolling
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onCommand
import dev.inmo.tgbotapi.types.BotCommand
import dev.inmo.tgbotapi.types.buttons.InlineKeyboardButtons.CallbackDataInlineKeyboardButton
import dev.inmo.tgbotapi.types.buttons.InlineKeyboardButtons.URLInlineKeyboardButton
import dev.inmo.tgbotapi.types.buttons.InlineKeyboardMarkup

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
                    description = "Приветственное меню",
                ),
                BotCommand(
                    command = "search",
                    description = "Поиск видео на YouTube по запросу",
                ),
                BotCommand(
                    command = "info",
                    description = "Информация о видео по ID",
                ),
                BotCommand(
                    command = "playlist",
                    description = "Получение видео из плейлиста по ID",
                ),
            )

            // Welcoming message
            onCommand("start", requireOnlyCommandInMessage = true) {
                startCommand(this, it)
            }

            // Searching video
            onCommand("search", requireOnlyCommandInMessage = true) {
                searchCommand(this, it, ytToken)
            }

            // Info about video by its id
            onCommand("info", requireOnlyCommandInMessage = true) {
                infoCommand(this, it, ytToken)
            }

            // Playlist videos by its id
            onCommand("playlist", requireOnlyCommandInMessage = true) {
                playlistCommand(this, it, ytToken)
            }

            // trying
            onCommand("try", requireOnlyCommandInMessage = true) {
                sendMessage(
                    chatId = it.chat.id,
                    text = "ktgbotapi is the best Kotlin Telegram Bot API library",
                    replyMarkup =
                        InlineKeyboardMarkup(
                            keyboard =
                                listOf(
                                    listOf(
                                        CallbackDataInlineKeyboardButton("I know", "know"),
                                        URLInlineKeyboardButton(
                                            "Learn more",
                                            "https://github.com/InsanusMokrassar/ktgbotapi",
                                        ),
                                    ),
                                ),
                        ),
                )
            }

            println(getMe())
        }.join()
}
