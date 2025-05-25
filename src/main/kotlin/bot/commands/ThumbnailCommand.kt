package bot.commands

import bot.commands.helper.getText
import dev.inmo.tgbotapi.extensions.api.send.media.sendPhoto
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.requests.abstracts.asMultipartFile
import dev.inmo.tgbotapi.types.message.content.TextMessage
import youtube.downloadAllThumbnails

suspend fun thumbnailsCommand(
    exec: BehaviourContext,
    command: TextMessage,
) {
    val videoId = getText(exec, command, "Send me list of youtube video IDs")
    val byteArrays = downloadAllThumbnails(videoId)

    byteArrays.forEach { bytes ->
        exec.sendPhoto(
            command.chat.id,
            bytes.asMultipartFile("thumbnail.jpg"),
        )
    }
}
