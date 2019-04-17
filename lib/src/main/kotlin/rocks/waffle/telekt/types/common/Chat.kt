package rocks.waffle.telekt.types

import kotlinx.serialization.Optional
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** This object represents a chat. */
@Serializable data class Chat(
    val id: Long,
    val type: String,
    @Optional val title: String? = null,
    @Optional val username: String? = null,
    @Optional @SerialName("first_name") val firstName: String? = null,
    @Optional @SerialName("last_name") val lastName: String? = null,
    @Optional @SerialName("all_members_are_administrators") val allMembersAreAdministrators: Boolean? = null,
    @Optional val photo: ChatPhoto? = null,
    @Optional val description: String? = null,
    @Optional @SerialName("invite_link") val inviteLink: String? = null,
    @Optional @SerialName("pinned_message") val pinnedMessage: Message? = null,
    @Optional @SerialName("sticker_set_name") val stickerSetName: String? = null,
    @Optional @SerialName("can_set_sticker_set") val canSetStickerSet: Boolean? = null
)