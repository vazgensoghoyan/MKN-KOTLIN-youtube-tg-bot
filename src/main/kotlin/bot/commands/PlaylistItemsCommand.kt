package bot.commands

import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.extensions.behaviour_builder.expectations.waitText
import dev.inmo.tgbotapi.requests.send.SendTextMessage
import dev.inmo.tgbotapi.types.buttons.InlineKeyboardButtons.URLInlineKeyboardButton
import dev.inmo.tgbotapi.types.buttons.InlineKeyboardMarkup
import dev.inmo.tgbotapi.types.message.content.TextMessage
import dev.inmo.tgbotapi.types.message.textsources.TextSource
import dev.inmo.tgbotapi.utils.bold
import dev.inmo.tgbotapi.utils.buildEntities
import dev.inmo.tgbotapi.utils.italic
import kotlinx.coroutines.flow.first
import youtube.PlaylistItem
import youtube.YtPlaylist
import youtube.getPlaylistInfo
import youtube.getPlaylistItems

suspend fun playlistCommand(
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

    val playlist = getPlaylistInfo(ytToken, playlistId)
    val videos = getPlaylistItems(ytToken, playlistId)

    exec.sendTextMessage(
        chatId = command.chat.id,
        entities = getResponseMessage(playlist, videos),
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
}

fun getResponseMessage(
    playlist: YtPlaylist,
    videos: List<PlaylistItem>,
): List<TextSource> {
    var playlistDescription: String? = playlist.snippet.description.trim()
    if (playlistDescription == "") playlistDescription = null

    return buildEntities {
        bold("Search results for playlist with ID '${playlist.id}':\n\n")

        bold(" Channel: ${playlist.snippet.channelTitle}\n")
        bold(" Playlist title: ${playlist.snippet.title}\n")
        playlistDescription?.let {
            bold(" Description (first <=100 chars):\n")
            +"  ${playlist.snippet.description.take(100)}\n"
        }
        +"\n"

        playlist.contentDetails?.let {
            italic(" Playlist items count: ${playlist.contentDetails.itemCount}\n\n")
        }

        videos.forEachIndexed { index, item ->
            bold("${index + 1}. ${item.snippet.title}\n")
            +"   Owner channel: ${item.snippet.videoOwnerChannelTitle}\n"
            +"   https://youtu.be/${item.snippet.resourceId.videoId}\n\n"
        }

        italic("Use /info to get details about specific video")
    }
}
