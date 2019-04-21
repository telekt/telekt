package rocks.waffle.telekt.network.requests.auto

import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import rocks.waffle.telekt.network.TelegramMethod
import rocks.waffle.telekt.network.requests.abstracts.SimpleRequest
import rocks.waffle.telekt.util.Recipient
import rocks.waffle.telekt.util.serializer


/**
 * [RestrictChatMember] request.
 * Use this method to restrict a user in a supergroup. The bot must be an administrator in the supergroup for this to work and must have the appropriate admin rights. Pass True for all boolean parameters to lift restrictions from a user. Returns True on success.
 * More: https://core.telegram.org/bots/api#restrictchatmember
 */
@Serializable data class RestrictChatMember(
    /** Unique identifier for the target chat or username of the target supergroup (in the format @supergroupusername) */
    @SerialName("chat_id") val chatId: Recipient,
    /** Unique identifier of the target user */
    @SerialName("user_id") val userId: Int,
    /** Date when restrictions will be lifted for the user, unix time. If user is restricted for more than 366 days or less than 30 seconds from the current time, they are considered to be restricted forever */
    @Optional @SerialName("until_date") val untilDate: Int? = null,
    /** Pass True, if the user can send text messages, contacts, locations and venues */
    @Optional @SerialName("can_send_messages") val canSendMessages: Boolean? = null,
    /** Pass True, if the user can send audios, documents, photos, videos, video notes and voice notes, implies can_send_messages */
    @Optional @SerialName("can_send_media_messages") val canSendMediaMessages: Boolean? = null,
    /** Pass True, if the user can send animations, games, stickers and use inline bots, implies can_send_media_messages */
    @Optional @SerialName("can_send_other_messages") val canSendOtherMessages: Boolean? = null,
    /** Pass True, if the user may add web page previews to their messages, implies can_send_media_messages */
    @Optional @SerialName("can_add_web_page_previews") val canAddWebPagePreviews: Boolean? = null
) : SimpleRequest<Unit>() {
    @Transient override val method = TelegramMethod.restrictChatMember
    @Transient override val resultDeserializer: KSerializer<out Unit> = Unit.serializer()
    override fun stringify(json: Json): String = json.stringify(RestrictChatMember.serializer(), this)
}
