package bot.commands

import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.extensions.behaviour_builder.expectations.waitText
import dev.inmo.tgbotapi.requests.send.SendTextMessage
import dev.inmo.tgbotapi.types.message.content.TextMessage
import io.ktor.util.toLowerCasePreservingASCIIRules
import kotlinx.coroutines.flow.first

suspend fun searchConcreteCommand(
    exec: BehaviourContext,
    command: TextMessage,
    ytToken: String,
) {
    val possibleValues = setOf("video", "playlist", "channel")

    var whatToSearch: String
    var isWhatToSearchRight: Boolean
    do {
        whatToSearch =
            exec
                .waitText(
                    SendTextMessage(
                        command.chat.id,
                        "Write down what you need to look for, separated by commas. Acceptable values: video, playlist, channel",
                    ),
                ).first()
                .text

        whatToSearch = whatToSearch.toLowerCasePreservingASCIIRules()
        val split = whatToSearch.split(", ").filter { it.isNotEmpty() }

        val f1 = split.size == split.toSet().size
        val f2 = split.all { it in possibleValues }

        isWhatToSearchRight = f1 && f2
    } while (!isWhatToSearchRight)

    // Calling search command with additional parameter
    searchCommand(exec, command, ytToken, whatToSearch)
}
