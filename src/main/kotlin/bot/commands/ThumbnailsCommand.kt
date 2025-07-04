package bot.commands

import bot.commands.helper.Helper
import dev.inmo.tgbotapi.extensions.api.send.media.sendPhoto
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.requests.abstracts.asMultipartFile
import dev.inmo.tgbotapi.types.message.content.TextMessage
import youtube.ThumbnailDownloader

class ThumbnailsCommand : IBotCommand {
    override val command = "thumbnails"
    override val description = "Get the best thumbnails for given video IDs"

    override suspend fun execute(
        exec: BehaviourContext,
        msg: TextMessage,
        ytToken: String,
    ) {
        val videoId = Helper.getText(exec, msg, "Send me list of YouTube video IDs (one per line)")

        val videoIds =
            videoId
                .split('\n')
                .map { it.trim() }
                .filter { it.isNotEmpty() }
                .take(10)

        val th = ThumbnailDownloader()
        val byteArrays = th.downloadThumbnailsForVideos(ytToken, videoIds)

        byteArrays.forEach { bytes ->
            exec.sendPhoto(
                msg.chat.id,
                bytes.asMultipartFile("thumbnail.jpg"),
            )
        }
    }
}
