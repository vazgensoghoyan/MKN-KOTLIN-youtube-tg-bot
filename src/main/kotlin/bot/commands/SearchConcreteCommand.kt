package bot.commands

import bot.commands.helper.getText
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.types.message.content.TextMessage

suspend fun searchConcreteCommand(
    exec: BehaviourContext,
    command: TextMessage,
    ytToken: String,
) {
    // Getting whatToSearch parameter
    var whatToSearch = ""

    while (!isSearchTypesValid(whatToSearch)) {
        whatToSearch =
            getText(
                exec,
                command,
                "Write down what you need to look for, separated by commas. Acceptable values: video, playlist, channel",
            )
    }

    whatToSearch = normalizeSearchTypes(whatToSearch)

    // Calling search command with additional parameter
    searchCommand(exec, command, ytToken, whatToSearch)
}

private val ACCEPTABLE_SEARCH_TYPES = setOf("video", "playlist", "channel")

private fun isSearchTypesValid(input: String): Boolean {
    val types =
        input
            .split(",")
            .map { it.trim().lowercase() }
            .filter { it.isNotBlank() }

    return types.isNotEmpty() &&
        types.size == types.toSet().size &&
        types.all { it in ACCEPTABLE_SEARCH_TYPES }
}

private fun normalizeSearchTypes(input: String): String =
    input
        .split(",")
        .map { it.trim().lowercase() }
        .distinct()
        .joinToString(",")
