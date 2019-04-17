package rocks.waffle.telekt.types

import kotlinx.serialization.Optional
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** This object contains information about one member of a chat. */
@Serializable data class ChatMember(
    val user: User,
    val status: String,
    @Optional @SerialName("until_date") val untilDate: Int? = null,
    @Optional @SerialName("can_be_edited") val canBeEdited: Boolean? = null,
    @Optional @SerialName("can_change_info") val canChangeInfo: Boolean? = null,
    @Optional @SerialName("can_post_messages") val canPostMessages: Boolean? = null,
    @Optional @SerialName("can_edit_messages") val canEditMessages: Boolean? = null,
    @Optional @SerialName("can_delete_messages") val canDeleteMessages: Boolean? = null,
    @Optional @SerialName("can_invite_users") val canInviteUsers: Boolean? = null,
    @Optional @SerialName("can_restrict_members") val canRestrictMembers: Boolean? = null,
    @Optional @SerialName("can_pin_messages") val canPinMessages: Boolean? = null,
    @Optional @SerialName("can_promote_members") val canPromoteMembers: Boolean? = null,
    @Optional @SerialName("can_send_messages") val canSendMessages: Boolean? = null,
    @Optional @SerialName("can_send_media_messages") val canSendMediaMessages: Boolean? = null,
    @Optional @SerialName("can_send_other_messages") val canSendOtherMessages: Boolean? = null,
    @Optional @SerialName("can_add_web_page_previews") val canAddWebPagePreviews: Boolean? = null
)