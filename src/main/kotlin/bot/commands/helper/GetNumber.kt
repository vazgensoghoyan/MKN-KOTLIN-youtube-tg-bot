package bot.commands.helper

import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.extensions.behaviour_builder.expectations.waitText
import dev.inmo.tgbotapi.requests.send.SendTextMessage
import dev.inmo.tgbotapi.types.buttons.ReplyKeyboardMarkup
import dev.inmo.tgbotapi.types.buttons.SimpleKeyboardButton
import dev.inmo.tgbotapi.types.message.content.TextMessage
import dev.inmo.tgbotapi.utils.matrix
import dev.inmo.tgbotapi.utils.row
import kotlinx.coroutines.flow.first

suspend fun getNumber(
    exec: BehaviourContext,
    command: TextMessage,
    defaultValue: Int,
    message: String,
    good: (Int) -> Boolean,
): Int {
    var number: Int
    do {
        number = exec
            .waitText(
                SendTextMessage(
                    command.chat.id,
                    message,
                    replyMarkup =
                        ReplyKeyboardMarkup(
                            matrix {
                                row {
                                    +SimpleKeyboardButton("Leave default value")
                                }
                            },
                            resizeKeyboard = true,
                        ),
                ),
            ).first()
            .text
            .takeIf { it != "Leave default value" }
            ?.toInt() ?: defaultValue
    } while (!good(number))

    return number
}
