/*
 * The contents of this file is free and unencumbered software released into the
 * public domain. For more information, please refer to <http://unlicense.org/>
 */

@file:JvmName("Main")

package gragas

import dev.kord.core.Kord
import java.lang.System.getenv
import java.lang.System.setProperty
import java.util.concurrent.Executors
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.DEBUG_PROPERTY_NAME
import kotlinx.coroutines.DEBUG_PROPERTY_VALUE_ON
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging

private val log = KotlinLogging.logger { }

fun main() {
  setProperty(DEBUG_PROPERTY_NAME, DEBUG_PROPERTY_VALUE_ON)

  val rootDispatcher = Executors.newFixedThreadPool(8).asCoroutineDispatcher()

  runBlocking(rootDispatcher + CoroutineName("gragas")) {
    startBot()
  }
}

suspend fun startBot() {
  log.info { "Starting bot..." }

  val token = getenv("DISCORD_TOKEN") ?: error("Can not find Bot token.")
  val kord = Kord(token) {
  }

  kord.login()
}