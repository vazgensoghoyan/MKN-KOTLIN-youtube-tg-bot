package bot.commands

import bot.commands.helper.Helper
import dev.inmo.tgbotapi.extensions.api.send.media.sendPhoto
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.requests.abstracts.asMultipartFile
import dev.inmo.tgbotapi.types.message.content.TextMessage
import youtube.downloadAllThumbnailVariants

public class ThumbnailCommand : IBotCommand {
    override val command = "thumbnail"
    override val description = "Get all video thumbnails of different qualities"

    override suspend fun execute(
        exec: BehaviourContext,
        msg: TextMessage,
        ytToken: String,
    ) {
        val videoId = Helper.getText(exec, msg, "Send me list of youtube video IDs")
        val byteArrays = downloadAllThumbnailVariants(videoId)

        byteArrays.forEach { bytes ->
            exec.sendPhoto(
                msg.chat.id,
                bytes.asMultipartFile("thumbnail.jpg"),
            )
        }
    }
}
