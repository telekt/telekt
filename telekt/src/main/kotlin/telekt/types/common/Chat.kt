package rocks.waffle.telekt.types

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** This object represents a chat. */
@Serializable data class Chat(
    val id: Long,
    val type: String,
    val title: String? = null,
    val username: String? = null,
    @SerialName("first_name") val firstName: String? = null,
    @SerialName("last_name") val lastName: String? = null,
    @SerialName("all_members_are_administrators") val allMembersAreAdministrators: Boolean? = null,
    val photo: ChatPhoto? = null,
    val description: String? = null,
    @SerialName("invite_link") val inviteLink: String? = null,
    @SerialName("pinned_message") val pinnedMessage: Message? = null,
    @SerialName("sticker_set_name") val stickerSetName: String? = null,
    @SerialName("can_set_sticker_set") val canSetStickerSet: Boolean? = null
)