package bot.commands.helper

import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.extensions.behaviour_builder.expectations.waitText
import dev.inmo.tgbotapi.requests.send.SendTextMessage
import dev.inmo.tgbotapi.types.message.content.TextMessage
import kotlinx.coroutines.flow.first

suspend fun getText(
    exec: BehaviourContext,
    command: TextMessage,
    message: String,
): String =
    exec
        .waitText(
            SendTextMessage(
                command.chat.id,
                message,
            ),
        ).first()
        .text
