package bot

import dev.inmo.tgbotapi.bot.ktor.telegramBot
import dev.inmo.tgbotapi.extensions.api.bot.getMe
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.buildBehaviourWithLongPolling
import dev.inmo.tgbotapi.extensions.behaviour_builder.expectations.waitText
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onCommand
import dev.inmo.tgbotapi.requests.send.SendTextMessage
import getVideoInfo
import kotlinx.coroutines.flow.first

suspend fun videoInfoBot(
    token: String,
    ytToken: String,
) {
    val bot = telegramBot(token)

    bot
        .buildBehaviourWithLongPolling {
            println(getMe())

            onCommand("start") {
                val videoId =
                    waitText(
                        SendTextMessage(it.chat.id, "Send me yt video id"),
                    ).first().text

                sendTextMessage(
                    it.chat.id,
                    getVideoInfo(ytToken, videoId),
                )
            }
        }.join()
}
