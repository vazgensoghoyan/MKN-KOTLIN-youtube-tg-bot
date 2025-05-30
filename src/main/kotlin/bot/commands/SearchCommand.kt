package bot.commands

import bot.commands.helper.getNumber
import bot.commands.helper.getText
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.types.buttons.ReplyKeyboardRemove
import dev.inmo.tgbotapi.types.message.content.TextMessage
import dev.inmo.tgbotapi.types.message.textsources.TextSource
import dev.inmo.tgbotapi.utils.bold
import dev.inmo.tgbotapi.utils.buildEntities
import dev.inmo.tgbotapi.utils.italic
import youtube.YoutubeSearch
import youtube.YtSearchListItem

class SearchCommand : IBotCommand {
    override val command = "search"
    override val description = "Search on YouTube on request"

    override suspend fun execute(
        exec: BehaviourContext,
        msg: TextMessage,
        ytToken: String,
    ) {
        val queryMessage = getText(exec, msg, "Send me query message for search")

        val maxResults: Int =
            getNumber(
                exec,
                msg,
                5,
                "Give me needed count of search results, less then 10. Default value is 5",
            ) { number -> 1 <= number && number <= 10 }

        // Searching video
        val searcher = YoutubeSearch(ytToken)
        val searchResults = searcher.youtubeSearch(queryMessage, maxResults)

        // If nothing was found
        if (searchResults.isEmpty()) {
            exec.sendTextMessage(
                msg.chat.id,
                "Nothing found for '$queryMessage'",
            )
            return
        }

        // Formatting the list of found videos
        val response = buildResponse(queryMessage, searchResults)

        // Sending the message
        exec.sendTextMessage(
            msg.chat.id,
            response,
            replyMarkup = ReplyKeyboardRemove(),
        )
    }

    // Formatting the list of found videos
    private fun buildResponse(
        queryMessage: String,
        searchResults: List<YtSearchListItem>,
    ): List<TextSource> =
        buildEntities {
            bold("Search results for '$queryMessage':\n\n")
            searchResults.forEachIndexed { index, item ->
                bold("${index + 1}. ")
                +item.toMessageEntities()
                +"\n\n"
            }
            italic("Use /info to get details about specific video")
        }

    // Formatting one item
    private fun YtSearchListItem.toMessageEntities(): List<TextSource> =
        when (id.kind) {
            "youtube#video" -> {
                buildEntities {
                    italic("(VIDEO)\t") + bold("${snippet.title}\n")
                    italic("   Channel: ") + "${snippet.channelTitle}\n"
                    italic("   Video description (first <=100 chars):\n")
                    +"«${snippet.description.take(100)}...»\n"
                    +"   https://youtu.be/${id.videoId}"
                }
            }

            "youtube#playlist" -> {
                buildEntities {
                    italic("(PLAYLIST)\t") + bold("${snippet.title}\n")
                    italic("   Channel:") + "${snippet.channelTitle}\n"
                    italic("   Playlist description (first <=100 chars):\n")
                    +"«${snippet.description.take(100)}...»\n"
                    +"   https://www.youtube.com/playlist?list=${id.playlistId}"
                }
            }

            "youtube#channel" -> {
                buildEntities {
                    italic("(CHANNEL)\t") + bold("${snippet.title}\n")
                    italic("   Channel description (first <=100 chars):\n")
                    +"«${snippet.description.take(100)}...»\n"
                    +"   https://www.youtube.com/channel/${id.channelId}"
                }
            }

            else -> throw Exception()
        }
}
