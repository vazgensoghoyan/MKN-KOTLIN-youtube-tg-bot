package bot.commands

import dev.inmo.tgbotapi.bot.RequestsExecutor
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.types.message.content.TextMessage

suspend fun startCommand(
    exec: RequestsExecutor,
    command: TextMessage,
) {
    exec.sendTextMessage(
        chatId = command.chat.id,
        text = "Welcome! Use commands:\n/search - find videos\n/info <videoId> - get video details",
    )
}
