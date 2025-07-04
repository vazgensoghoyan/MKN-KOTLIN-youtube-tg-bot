package bot.commands

import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.types.message.content.TextMessage

interface IBotCommand {
    val command: String
    val description: String

    suspend fun execute(
        exec: BehaviourContext,
        msg: TextMessage,
        ytToken: String,
    )
}
