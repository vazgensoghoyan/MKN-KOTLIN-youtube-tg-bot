package bot.commands

import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.types.message.content.TextMessage

const val WELCOMING_TEXT =
    """
Welcome! Use commands:
/search - Search on YouTube on request
/search_concrete - Search for specific entities (videos, playlists, channels)
/info - Information about the video by ID
/playlist - Information about the playlist and videos in it by ID
/thumbnail - Get all video thumbnails of different qualities
/thumbnails - Get the best thumbnails for given videos
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
