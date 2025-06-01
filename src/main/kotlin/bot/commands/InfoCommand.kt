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
import youtube.models.VideoInfo
import youtube.VideoInfoGetter

class InfoCommand : IBotCommand {
    override val command = "info"
    override val description = "Information about the video by ID"

    override suspend fun execute(
        exec: BehaviourContext,
        msg: TextMessage,
        ytToken: String,
    ) {
        val videoId = Helper.getText(exec, msg, "Send me ID of youtube video")

        try {
            val g = VideoInfoGetter(ytToken, videoId)
            val vid = g.getVideoInfo()

            exec.sendTextMessage(
                chatId = msg.chat.id,
                entities = vid.toMessageEntities(),
                replyMarkup = vid.toButton(),
            )
        } catch (_: Exception) {
            exec.sendTextMessage(
                msg.chat.id,
                "No video of ID '$videoId' found",
            )
        }
    }

    // Video to button in bot
    private fun VideoInfo.toButton(): InlineKeyboardMarkup =
        InlineKeyboardMarkup(
            keyboard =
                listOf(
                    listOf(
                        URLInlineKeyboardButton(
                            "Watch it",
                            "https://youtu.be/$id",
                        ),
                    ),
                ),
        )

    // Constructing response message from data
    private fun VideoInfo.toMessageEntities(): List<TextSource> {
        val videoInfo = this

        return buildEntities(" ") {
            bold("Video information for ${videoInfo.id}:\n\n") +
                italic("Video title: ") + "${videoInfo.snippet.title}\n" +
                italic("Channel name: ") + "${videoInfo.snippet.channelTitle}\n" +
                italic("Channel ID: ") + "${videoInfo.snippet.channelId}\n\n" +

                italic("Published: ") + "${videoInfo.snippet.publishedAt}\n" +
                italic("Views: ") + "${videoInfo.statistics.viewCount}\n" +
                italic("Likes: ") + "${videoInfo.statistics.likeCount}\n" +
                italic("Comments: ") + "${videoInfo.statistics.commentCount}\n\n" +

                italic("Description (first <=100 chars):\n") +
                "«${videoInfo.snippet.description.take(100)}...»"
        }
    }
}
