package bot.commands

import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.types.message.content.TextMessage

const val WELCOMING_TEXT =
    """
Welcome! Use commands:
/search - search in youtube (videos, playlists, channels)
/info - get video details
/playlist - get info about playlist and videos from it by its id
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
