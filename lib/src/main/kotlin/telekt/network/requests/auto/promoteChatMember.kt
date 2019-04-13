package telekt.network.requests.auto

import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import telekt.network.TelegramMethod
import telekt.network.requests.abstracts.SimpleRequest
import telekt.util.Recipient
import telekt.util.serializer


/**
 * [PromoteChatMember] request.
 * Use this method to promote or demote a user in a supergroup or a channel. The bot must be an administrator in the chat for this to work and must have the appropriate admin rights. Pass False for all boolean parameters to demote a user. Returns True on success.
 * More: https://core.telegram.org/bots/api#promotechatmember
 */
@Serializable data class PromoteChatMember(
    /** Unique identifier for the target chat or username of the target channel (in the format @channelusername) */
    @SerialName("chat_id") val chatId: Recipient,
    /** Unique identifier of the target user */
    @SerialName("user_id") val userId: Int,
    /** Pass True, if the administrator can change chat title, photo and other settings */
    @Optional @SerialName("can_change_info") val canChangeInfo: Boolean? = null,
    /** Pass True, if the administrator can create channel posts, channels only */
    @Optional @SerialName("can_post_messages") val canPostMessages: Boolean? = null,
    /** Pass True, if the administrator can edit messages of other users and can pin messages, channels only */
    @Optional @SerialName("can_edit_messages") val canEditMessages: Boolean? = null,
    /** Pass True, if the administrator can delete messages of other users */
    @Optional @SerialName("can_delete_messages") val canDeleteMessages: Boolean? = null,
    /** Pass True, if the administrator can invite new users to the chat */
    @Optional @SerialName("can_invite_users") val canInviteUsers: Boolean? = null,
    /** Pass True, if the administrator can restrict, ban or unban chat members */
    @Optional @SerialName("can_restrict_members") val canRestrictMembers: Boolean? = null,
    /** Pass True, if the administrator can pin messages, supergroups only */
    @Optional @SerialName("can_pin_messages") val canPinMessages: Boolean? = null,
    /** Pass True, if the administrator can add new administrators with a subset of his own privileges or demote administrators that he has promoted, directly or indirectly (promoted by administrators that were appointed by him) */
    @Optional @SerialName("can_promote_members") val canPromoteMembers: Boolean? = null
) : SimpleRequest<Unit>() {
    @Transient override val method = TelegramMethod.promoteChatMember
    @Transient override val resultDeserializer: KSerializer<out Unit> = Unit.serializer()
    override fun stringify(json: Json): String = json.stringify(PromoteChatMember.serializer(), this)
}
