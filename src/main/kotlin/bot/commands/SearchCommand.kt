package bot.commands

import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.extensions.behaviour_builder.expectations.waitText
import dev.inmo.tgbotapi.requests.send.SendTextMessage
import dev.inmo.tgbotapi.types.buttons.ReplyKeyboardMarkup
import dev.inmo.tgbotapi.types.buttons.ReplyKeyboardRemove
import dev.inmo.tgbotapi.types.buttons.SimpleKeyboardButton
import dev.inmo.tgbotapi.types.message.content.TextMessage
import dev.inmo.tgbotapi.types.message.textsources.TextSource
import dev.inmo.tgbotapi.utils.bold
import dev.inmo.tgbotapi.utils.buildEntities
import dev.inmo.tgbotapi.utils.italic
import dev.inmo.tgbotapi.utils.matrix
import dev.inmo.tgbotapi.utils.row
import kotlinx.coroutines.flow.first
import youtube.YoutubeSearch
import youtube.YtSearchListItem

suspend fun searchCommand(
    exec: BehaviourContext,
    command: TextMessage,
    ytToken: String,
    whatToSearch: String = "video,playlist,channel",
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

    var maxResultsCount: Int

    do {
        maxResultsCount = exec
            .waitText(
                SendTextMessage(
                    command.chat.id,
                    "Give me needed count of search results, less then 10. Default value is 5",
                    replyMarkup =
                        ReplyKeyboardMarkup(
                            matrix {
                                row {
                                    +SimpleKeyboardButton("Leave default value")
                                }
                            },
                            resizeKeyboard = true,
                        ),
                ),
            ).first()
            .text
            .takeIf { it != "Leave default value" }
            ?.toInt() ?: 5
    } while (maxResultsCount > 10)

    // Searching video
    val searcher = YoutubeSearch(ytToken, whatToSearch)
    val searchResults = searcher.youtubeSearch(queryMessage, maxResultsCount)

    // If nothing was found
    if (searchResults.isEmpty()) {
        exec.sendTextMessage(
            chatId = command.chat.id,
            text = "Nothing found for '$queryMessage'",
        )
        return
    }

    // Formatting the list of found videos
    val response =
        buildEntities {
            bold("Search results for '$queryMessage':\n\n")
            searchResults.forEachIndexed { index, item ->
                bold("${index + 1}. ")
                +item.toMessageEntities()
                +"\n\n"
            }
            italic("Use /info to get details about specific video")
        }

    // Sending the message
    exec.sendTextMessage(
        chatId = command.chat.id,
        entities = response,
        replyMarkup = ReplyKeyboardRemove(),
    )
}

fun YtSearchListItem.toMessageEntities(): List<TextSource> =
    when (id.kind) {
        "youtube#video" -> {
            buildEntities {
                italic("(VIDEO)\t") + bold("${snippet.title}\n")
                italic("   Channel: ") + "${snippet.channelTitle}\n"
                italic("   Video description (first <=100 chars):\n")
                +"«${snippet.description.take(100)}\n...»"
                +"   https://youtu.be/${id.videoId}\n\n"
            }
        }

        "youtube#playlist" -> {
            buildEntities {
                italic("(PLAYLIST)\t") + bold("${snippet.title}\n")
                italic("   Channel:") + "${snippet.channelTitle}\n"
                italic("   Playlist description (first <=100 chars):\n")
                +"«${snippet.description.take(100)}\n...»"
                +"   https://www.youtube.com/playlist?list=${id.playlistId}"
            }
        }

        "youtube#channel" -> {
            buildEntities {
                italic("(CHANNEL)\t") + bold("${snippet.title}\n")
                italic("   Channel description (first <=100 chars):\n")
                +"«${snippet.description.take(100)}\n...»"
                +"   https://www.youtube.com/channel/${id.channelId}"
            }
        }

        else -> throw Exception()
    }
