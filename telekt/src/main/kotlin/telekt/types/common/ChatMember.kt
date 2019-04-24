package rocks.waffle.telekt.types

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** This object contains information about one member of a chat. */
@Serializable data class ChatMember(
    val user: User,
    val status: String,
    @SerialName("until_date") val untilDate: Int? = null,
    @SerialName("can_be_edited") val canBeEdited: Boolean? = null,
    @SerialName("can_change_info") val canChangeInfo: Boolean? = null,
    @SerialName("can_post_messages") val canPostMessages: Boolean? = null,
    @SerialName("can_edit_messages") val canEditMessages: Boolean? = null,
    @SerialName("can_delete_messages") val canDeleteMessages: Boolean? = null,
    @SerialName("can_invite_users") val canInviteUsers: Boolean? = null,
    @SerialName("can_restrict_members") val canRestrictMembers: Boolean? = null,
    @SerialName("can_pin_messages") val canPinMessages: Boolean? = null,
    @SerialName("can_promote_members") val canPromoteMembers: Boolean? = null,
    @SerialName("is_member") val isMember: Boolean? = null,
    @SerialName("can_send_messages") val canSendMessages: Boolean? = null,
    @SerialName("can_send_media_messages") val canSendMediaMessages: Boolean? = null,
    @SerialName("can_send_other_messages") val canSendOtherMessages: Boolean? = null,
    @SerialName("can_add_web_page_previews") val canAddWebPagePreviews: Boolean? = null
)