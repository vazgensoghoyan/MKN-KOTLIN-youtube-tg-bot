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

class StartCommand : IBotCommand {
    override val command = "start"
    override val description = "Welcoming menu"

    override suspend fun execute(
        exec: BehaviourContext,
        msg: TextMessage,
    ) {
        exec.sendTextMessage(
            chatId = msg.chat.id,
            text = WELCOMING_TEXT,
        )
    }
}
