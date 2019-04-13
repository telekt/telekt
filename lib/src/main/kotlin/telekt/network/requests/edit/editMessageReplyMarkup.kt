package telekt.network.requests.edit

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Optional
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import telekt.network.TelegramMethod
import telekt.network.requests.abstracts.SimpleRequest
import telekt.types.InlineKeyboardMarkup
import telekt.types.Message
import telekt.util.Recipient
import telekt.util.serializer


/**
 * [EditMessageReplyMarkup] inline request.
 * Use this method to edit only the reply markup of messages sent via the bot (for inline bots).
 * More: https://core.telegram.org/bots/api#editmessagereplymarkup
 */
@Serializable data class EditMessageReplyMarkupInline(
    /** Identifier of the inline message */
    @SerialName("inline_message_id") val inlineMessageId: String,
    /** A JSON-serialized object for an inline keyboard. */
    @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null
) : SimpleRequest<Unit>() {
    @Transient override val method = TelegramMethod.editMessageReplyMarkup
    @Transient override val resultDeserializer: KSerializer<out Unit> = Unit.serializer()
    override fun stringify(json: Json): String = json.stringify(EditMessageReplyMarkupInline.serializer(), this)
}


/**
 * [EditMessageReplyMarkup] request.
 * Use this method to edit only the reply markup of messages sent by the bot.
 * On success, the edited Message is returned.
 * More: https://core.telegram.org/bots/api#editmessagereplymarkup
 */
@Serializable data class EditMessageReplyMarkup(
    /** Unique identifier for the target chat or username of the target channel (in the format @channelusername) */
    @SerialName("chat_id") val chatId: Recipient,
    /** Identifier of the sent message */
    @SerialName("message_id") val messageId: Int,
    /** A JSON-serialized object for an inline keyboard. */
    @Optional @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null
) : SimpleRequest<Message>() {
    @Transient override val method = TelegramMethod.editMessageReplyMarkup
    @Transient override val resultDeserializer: KSerializer<out Message> = Message.serializer()
    override fun stringify(json: Json): String = json.stringify(EditMessageReplyMarkup.serializer(), this)
}
