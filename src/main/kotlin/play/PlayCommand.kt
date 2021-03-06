/*
 * The contents of this file is free and unencumbered software released into the
 * public domain. For more information, please refer to <http://unlicense.org/>
 */

@file:OptIn(KordVoice::class)

package gragas.play

import dev.kord.common.annotation.KordVoice
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.rest.builder.interaction.string
import gragas.commands.Command
import gragas.commands.CommandScope

class PlayCommand(val trackService: TrackService) : Command(
  name = "play",
  description = "Plays a song or a track with the specified url",
) {
  init {
    settings {
      string("url", "The specified song") {
        required = true
      }
    }
  }

  @Suppress("ControlFlowWithEmptyBody")
  override suspend fun CommandScope.execute(event: GuildChatInputCommandInteractionCreateEvent) {
    val interaction = event.interaction
    val command = interaction.command

    val channel = interaction.getCurrentVoiceChannel()

    val song = command.strings["url"] ?: error("unreachable")

    trackService.get(channel).play(song)

    interaction.deferPublicResponse().respond {
      content = "You have queued the song <$song> in channel ${channel.data.name.value}"
    }
  }
}
