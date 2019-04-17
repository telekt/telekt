package rocks.waffle.telekt.network.requests.auto

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Optional
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import rocks.waffle.telekt.network.TelegramMethod
import rocks.waffle.telekt.network.requests.abstracts.SimpleRequest
import rocks.waffle.telekt.types.InlineKeyboardMarkup
import rocks.waffle.telekt.util.Recipient
import rocks.waffle.telekt.types.Poll


/**
 * [StopPoll] request.
 * Use this method to stop a poll which was sent by the bot.
 * On success, the stopped Poll with the final results is returned.
 *
 * More: https://core.telegram.org/bots/api#stoppoll
 */
@Serializable data class StopPoll(
    /** Unique identifier for the target chat or username of the target channel (in the format @channelusername) */
    @SerialName("chat_id") val chatId: Recipient,
    /** Identifier of the original message with the poll */
    @SerialName("message_id") val messageId: Int,
    /** A JSON-serialized object for a new message inline keyboard. */
    @Optional @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null
) : SimpleRequest<Poll>() {
    @Transient override val method = TelegramMethod.stopPoll
    @Transient override val resultDeserializer: KSerializer<out Poll> = Poll.serializer()
    override fun stringify(json: Json): String = json.stringify(StopPoll.serializer(), this)
}