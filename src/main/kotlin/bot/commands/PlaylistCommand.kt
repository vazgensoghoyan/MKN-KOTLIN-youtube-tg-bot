package bot.commands

import bot.commands.helper.Helper
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.types.buttons.InlineKeyboardButtons.URLInlineKeyboardButton
import dev.inmo.tgbotapi.types.buttons.InlineKeyboardMarkup
import dev.inmo.tgbotapi.types.message.content.TextMessage
import dev.inmo.tgbotapi.types.message.textsources.TextSource
import dev.inmo.tgbotapi.utils.bold
import dev.inmo.tgbotapi.utils.buildEntities
import dev.inmo.tgbotapi.utils.italic
import youtube.PlaylistItem
import youtube.YtPlaylist
import youtube.getPlaylistInfo
import youtube.getPlaylistItems

class PlaylistCommand : IBotCommand {
    override val command = "playlist"
    override val description = "Information about the playlist and videos in it by ID"

    override suspend fun execute(
        exec: BehaviourContext,
        msg: TextMessage,
        ytToken: String,
    ) {
        val playlistId = Helper.getText(exec, msg, "Send me ID of youtube playlist")
        val maxResults: Int =
            Helper.getNumber(
                exec,
                msg,
                5,
                "Give me needed count of search results, less then 10. Default value is 5",
            ) { number -> 1 <= number && number <= 10 }

        val playlist = getPlaylistInfo(ytToken, playlistId)
        val videos = getPlaylistItems(ytToken, playlistId, maxResults)

        exec.sendTextMessage(
            chatId = msg.chat.id,
            entities = getResponseMessage(playlist, videos),
            replyMarkup = playlist.toButton(),
        )
    }

    // Playlist to button in bot
    private fun YtPlaylist.toButton(): InlineKeyboardMarkup =
        InlineKeyboardMarkup(
            keyboard =
                listOf(
                    listOf(
                        URLInlineKeyboardButton(
                            "Open playlist",
                            "https://www.youtube.com/playlist?list=$id",
                        ),
                    ),
                ),
        )

    // Constructing response message from data
    private fun getResponseMessage(
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
}
