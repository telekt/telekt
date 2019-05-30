package rocks.waffle.telekt.contrib.filters

import rocks.waffle.telekt.dispatcher.Filter
import rocks.waffle.telekt.dispatcher.HandlerScope
import rocks.waffle.telekt.types.Message


class CommandFilter(
    command: String,
    vararg commands: String,
    private val prefixes: Array<Char> = arrayOf('/'),
    private val ignoreCase: Boolean = false,
    private val ignoreMention: Boolean = false
) : Filter<Message>() {
    private val commands = listOf(command, *commands)

    override suspend fun test(scope: HandlerScope, value: Message): Boolean {
        val fullCommand = value.text?.split(' ', limit = 2)?.get(0) ?: return false

        val prefix = fullCommand[0]
        if (prefix !in prefixes) return false

        val (command, mention) = extractCommandAndMention(fullCommand)
        if ((if (ignoreCase) command.toLowerCase() else command) !in commands) return false
        if (!ignoreMention && mention != null && scope.bot.me.await().username?.toLowerCase() != mention.toLowerCase()) return false

        return true
    }

    companion object {
        private fun extractCommandAndMention(text: String): Pair<String, String?> {
            val commandWithMention = text.substring(1).split('@', limit = 2)
            return commandWithMention[0] to commandWithMention.getOrNull(1)
        }
    }
}
