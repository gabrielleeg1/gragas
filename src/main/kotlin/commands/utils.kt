/*
 * The contents of this file is free and unencumbered software released into the
 * public domain. For more information, please refer to <http://unlicense.org/>
 */

package gragas.commands

import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map

suspend fun Kord.register(command: Command) {
  events
    .filterIsInstance<GuildChatInputCommandInteractionCreateEvent>()
    .filter { it.interaction.command.rootName == command.name }
    .map {
      try {
        with(command) {
          with(CommandScope()) { execute(it) }
        }
      } catch (failure: CommandFailure) {
        it.interaction.deferPublicResponse().respond(failure.builder)
      }
    }
    .launchIn(this)

  createGlobalChatInputCommand(
    name = command.name,
    description = command.description,
    builder = command.settings,
  )
}
