package bot.commands.helper

import bot.commands.IBotCommand
import bot.commands.InfoCommand
import bot.commands.PlaylistCommand
import bot.commands.SearchCommand
import bot.commands.SearchConcreteCommand
import bot.commands.StartCommand
import bot.commands.ThumbnailCommand
import bot.commands.ThumbnailsCommand

object CommandFactory {
    fun createAllCommands(): List<IBotCommand> =
        listOf(
            StartCommand(),
            SearchCommand(),
            SearchConcreteCommand(),
            InfoCommand(),
            PlaylistCommand(),
            ThumbnailCommand(),
            ThumbnailsCommand(),
        )
}
