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
import youtube.VideoInfo
import youtube.getVideoInfo

suspend fun infoCommand(
    exec: BehaviourContext,
    command: TextMessage,
    ytToken: String,
) {
    val videoId =
        exec
            .waitText(
                SendTextMessage(
                    command.chat.id,
                    "Send me ID of youtube video",
                ),
            ).first()
            .text

    val vid = getVideoInfo(ytToken, videoId)

    exec.sendTextMessage(
        chatId = command.chat.id,
        entities = vid.toMessageEntities(),
        replyMarkup =
            InlineKeyboardMarkup(
                keyboard =
                    listOf(
                        listOf(
                            URLInlineKeyboardButton(
                                "Watch it",
                                "https://youtu.be/${vid.id}",
                            ),
                        ),
                    ),
            ),
    )
}

fun VideoInfo.toMessageEntities(): List<TextSource> {
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
