package bot.commands

import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.types.message.content.TextMessage

const val WELCOMING_TEXT =
    """
Welcome! Use commands:
/search - find videos
/info - get video details
/playlist_items - get videos from playlist
    """

suspend fun startCommand(
    exec: BehaviourContext,
    command: TextMessage,
) {
    exec.sendTextMessage(
        chatId = command.chat.id,
        text = WELCOMING_TEXT,
    )
}
