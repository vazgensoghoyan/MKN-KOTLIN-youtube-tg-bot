package bot.commands

import bot.commands.helper.getText
import dev.inmo.tgbotapi.extensions.api.send.media.sendPhoto
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.requests.abstracts.asMultipartFile
import dev.inmo.tgbotapi.types.message.content.TextMessage
import youtube.downloadThumbnailsForVideos

suspend fun thumbnailsCommand(
    exec: BehaviourContext,
    command: TextMessage,
    ytToken: String,
) {
    val videoId = getText(exec, command, "Send me list of YouTube video IDs (one per line)")

    val videoIds =
        videoId
            .split('\n')
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .take(10)

    val byteArrays = downloadThumbnailsForVideos(ytToken, videoIds)

    byteArrays.forEach { bytes ->
        exec.sendPhoto(
            command.chat.id,
            bytes.asMultipartFile("thumbnail.jpg"),
        )
    }
}
