package bot.commands

import dev.inmo.tgbotapi.bot.RequestsExecutor
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.types.message.content.TextMessage
import youtube.toMessage
import youtube.videoInfo

suspend fun infoCommand(
    exec: RequestsExecutor,
    command: TextMessage,
    ytToken: String,
) {
    try {
        val videoId =
            command.content.text
                .removePrefix("/info")
                .trim()

        if (videoId.isEmpty()) {
            exec.sendTextMessage(
                chatId = command.chat.id,
                text = "Please specify video ID after /info command",
            )
            return
        }

        exec.sendTextMessage(
            chatId = command.chat.id,
            text = videoInfo(ytToken, videoId).toMessage(),
        )
    } catch (e: Exception) {
        e.printStackTrace()
        exec.sendTextMessage(
            chatId = command.chat.id,
            text = "Error fetching video info",
        )
    }
}
