package bot.commands

import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.types.message.content.TextMessage

class StartCommand : IBotCommand {
    override val command = "start"
    override val description = "Welcoming menu"

    private val welcomingMenu =
        """
Welcome! Use commands:
/search - Search on YouTube on request
/search_concrete - Search for specific entities (videos, playlists, channels)
/info - Information about the video by ID
/playlist - Information about the playlist and videos in it by ID
/thumbnail - Get all video thumbnails of different qualities
/thumbnails - Get the best thumbnails for given videos
        """

    override suspend fun execute(
        exec: BehaviourContext,
        msg: TextMessage,
        ytToken: String,
    ) {
        exec.sendTextMessage(
            chatId = msg.chat.id,
            text = welcomingMenu,
        )
    }
}
