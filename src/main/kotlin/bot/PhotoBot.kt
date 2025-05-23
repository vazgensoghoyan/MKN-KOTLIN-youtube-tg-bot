package bot

import dev.inmo.tgbotapi.bot.ktor.telegramBot
import dev.inmo.tgbotapi.extensions.api.bot.getMe
import dev.inmo.tgbotapi.extensions.api.send.media.sendPhoto
import dev.inmo.tgbotapi.extensions.behaviour_builder.buildBehaviourWithLongPolling
import dev.inmo.tgbotapi.extensions.behaviour_builder.expectations.waitPhoto
import dev.inmo.tgbotapi.extensions.behaviour_builder.expectations.waitText
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onCommand
import dev.inmo.tgbotapi.requests.send.SendTextMessage
import dev.inmo.tgbotapi.types.buttons.SimpleKeyboardButton
import dev.inmo.tgbotapi.utils.bold
import dev.inmo.tgbotapi.utils.buildEntities
import dev.inmo.tgbotapi.utils.matrix
import dev.inmo.tgbotapi.utils.row
import kotlinx.coroutines.flow.first

suspend fun photoBot(token: String) {
    val bot = telegramBot(token)

    bot
        .buildBehaviourWithLongPolling {
            println(getMe())

            val nameReplyMarkup =
                dev.inmo.tgbotapi.types.buttons.ReplyKeyboardMarkup(
                    matrix {
                        row {
                            +SimpleKeyboardButton("nope")
                        }
                    },
                )
            onCommand("start") {
                val photo =
                    waitPhoto(
                        SendTextMessage(it.chat.id, "Send me your photo please"),
                    ).first()

                val name =
                    waitText(
                        SendTextMessage(
                            it.chat.id,
                            "Send me your name or choose \"nope\"",
                            replyMarkup = nameReplyMarkup,
                        ),
                    ).first().text.takeIf { msg -> msg != "nope" }

                sendPhoto(
                    it.chat,
                    photo.mediaCollection,
                    entities =
                        buildEntities {
                            if (name != null) bold(name) // may be collapsed up to name ?.let(::regular)
                        },
                )
            }
        }.join()
}
