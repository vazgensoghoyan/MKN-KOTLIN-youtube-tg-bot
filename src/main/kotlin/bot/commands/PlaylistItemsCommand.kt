package bot.commands

import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.extensions.behaviour_builder.expectations.waitText
import dev.inmo.tgbotapi.requests.send.SendTextMessage
import dev.inmo.tgbotapi.types.buttons.InlineKeyboardButtons.URLInlineKeyboardButton
import dev.inmo.tgbotapi.types.buttons.InlineKeyboardMarkup
import dev.inmo.tgbotapi.types.message.content.TextMessage
import dev.inmo.tgbotapi.utils.bold
import dev.inmo.tgbotapi.utils.buildEntities
import dev.inmo.tgbotapi.utils.italic
import kotlinx.coroutines.flow.first
import youtube.getPlaylistInfo
import youtube.getPlaylistItems

suspend fun playlistItemsCommand(
    exec: BehaviourContext,
    command: TextMessage,
    ytToken: String,
) {
    val playlistId =
        exec
            .waitText(
                SendTextMessage(
                    command.chat.id,
                    "Send me ID of youtube playlist",
                ),
            ).first()
            .text

    try {
        val playlist = getPlaylistInfo(ytToken, playlistId)

        if (playlist == null) {
            throw Exception()
        }

        val videos = getPlaylistItems(ytToken, playlistId)

        val response =
            buildEntities {
                bold("Search results for playlist with ID '$playlistId':\n\n")

                bold(" Channel: ${playlist.snippet.channelTitle}\n")
                bold(" Playlist title: ${playlist.snippet.title}\n")
                playlist.contentDetails?.let {
                    " Playlist items count: ${playlist.contentDetails.itemCount}\n"
                }
                +"\n"

                videos.forEachIndexed { index, item ->
                    bold("${index + 1}. ${item.snippet.title}\n")
                    +"   Owner channel: ${item.snippet.videoOwnerChannelTitle}\n"
                    +"   https://youtu.be/${item.snippet.resourceId.videoId}\n\n"
                }

                italic("Use /info to get details about specific video")
            }

        exec.sendTextMessage(
            chatId = command.chat.id,
            entities = response,
            replyMarkup =
                InlineKeyboardMarkup(
                    keyboard =
                        listOf(
                            listOf(
                                URLInlineKeyboardButton(
                                    "Open playlist",
                                    "https://www.youtube.com/playlist?list=$playlistId",
                                ),
                            ),
                        ),
                ),
        )
    } catch (e: Exception) {
        exec.sendTextMessage(
            chatId = command.chat.id,
            text = "Playlist with ID '$playlistId' was not found!",
        )
    }
}
