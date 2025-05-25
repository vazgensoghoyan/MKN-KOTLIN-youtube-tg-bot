package bot.commands

import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.extensions.behaviour_builder.expectations.waitText
import dev.inmo.tgbotapi.requests.send.SendTextMessage
import dev.inmo.tgbotapi.types.message.content.TextMessage
import dev.inmo.tgbotapi.utils.bold
import dev.inmo.tgbotapi.utils.buildEntities
import dev.inmo.tgbotapi.utils.italic
import kotlinx.coroutines.flow.first
import youtube.searchVideos

suspend fun searchCommand(
    exec: BehaviourContext,
    command: TextMessage,
    ytToken: String,
) {
    val queryMessage =
        exec
            .waitText(
                SendTextMessage(
                    command.chat.id,
                    "Send me query message for search",
                ),
            ).first()
            .text

    // Searching video
    val searchResults = searchVideos(ytToken, queryMessage)

    if (searchResults.isEmpty()) {
        exec.sendTextMessage(
            chatId = command.chat.id,
            text = "No videos found for '$queryMessage'",
        )
        return
    }

    // Formatting the list of found videos
    val response =
        buildEntities {
            bold("Search results for '$queryMessage':\n\n")
            searchResults.forEachIndexed { index, item ->
                bold("${index + 1}. ${item.snippet.title}\n") +
                    "   Channel: ${item.snippet.channelTitle}\n" +
                    "   https://youtu.be/${item.id.videoId}\n\n"
            }
            italic("Use /info to get details about specific video")
        }

    // Sending the message
    exec.sendTextMessage(
        chatId = command.chat.id,
        entities = response,
    )
}
