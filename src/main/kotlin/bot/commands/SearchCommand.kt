package bot.commands

import dev.inmo.tgbotapi.bot.RequestsExecutor
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.types.message.content.TextMessage
import youtube.searchVideos

suspend fun searchCommand(
    exec: RequestsExecutor,
    command: TextMessage,
    ytToken: String,
) {
    try {
        val queryMessage =
            command.content.text
                .removePrefix("/search")
                .trim()

        if (queryMessage.isEmpty()) {
            exec.sendTextMessage(
                chatId = command.chat.id,
                text = "Please specify search query after /search command",
            )
            return
        }

        // Ищем видео
        val searchResults = searchVideos(ytToken, queryMessage)

        if (searchResults.isEmpty()) {
            exec.sendTextMessage(
                chatId = command.chat.id,
                text = "No videos found for '$queryMessage'",
            )
            return
        }

        // Формируем список найденных видео
        val response =
            buildString {
                append("Search results for '$queryMessage':\n\n")
                searchResults.forEachIndexed { index, item ->
                    append("${index + 1}. ${item.snippet.title}\n")
                    append("   Channel: ${item.snippet.channelTitle}\n")
                    append("   https://youtu.be/${item.id.videoId}\n\n")
                }
                append("Use /info <videoId> to get details about specific video")
            }

        exec.sendTextMessage(
            chatId = command.chat.id,
            text = response,
        )
    } catch (e: Exception) {
        e.printStackTrace()
        exec.sendTextMessage(
            chatId = command.chat.id,
            text = "Error during search. Please try again later.",
        )
    }
}
