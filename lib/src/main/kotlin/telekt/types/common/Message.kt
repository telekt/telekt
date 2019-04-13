package telekt.types

import kotlinx.serialization.Optional
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import telekt.contrib.filters.TextableTelegramEvent
import telekt.types.enums.ContentType
import telekt.types.passport.PassportData


/** This object represents a message. */
@Serializable data class Message(
    @SerialName("message_id") val messageId: Int,
    val date: Int,
    val chat: Chat,
    @Optional val from: User? = null,
    @Optional @SerialName("forward_from") val forwardFrom: User? = null,
    @Optional @SerialName("forward_from_chat") val forwardFromChat: Chat? = null,
    @Optional @SerialName("forward_from_message_id") val forwardFromMessageId: Int? = null,
    @Optional @SerialName("forward_signature") val forwardSignature: String? = null,
    @Optional @SerialName("forward_date") val forwardDate: Int? = null,
    @Optional @SerialName("reply_to_message") val replyToMessage: Message? = null,
    @Optional @SerialName("edit_date") val editDate: Int? = null,
    @Optional @SerialName("media_group_id") val mediaGroupId: String? = null,
    @Optional @SerialName("author_signature") val authorSignature: String? = null,
    @Optional val text: String? = null,
    @Optional val entities: Array<MessageEntity>? = null,
    @Optional @SerialName("caption_entities") val captionEntities: Array<MessageEntity>? = null,
    @Optional val audio: Audio? = null,
    @Optional val document: Document? = null,
    @Optional val animation: Animation? = null,
    @Optional val game: Game? = null,
    @Optional val photo: Array<PhotoSize>? = null,
    @Optional val sticker: Sticker? = null,
    @Optional val video: Video? = null,
    @Optional val voice: Voice? = null,
    @Optional @SerialName("video_note") val videoNote: VideoNote? = null,
    @Optional val caption: String? = null,
    @Optional val contact: Contact? = null,
    @Optional val location: Location? = null,
    @Optional val venue: Venue? = null,
    @Optional @SerialName("new_chat_members") val newChatMembers: Array<User>? = null,
    @Optional @SerialName("left_chat_member") val leftChatMember: User? = null,
    @Optional @SerialName("new_chat_title") val newChatTitle: String? = null,
    @Optional @SerialName("new_chat_photo") val newChatPhoto: Array<PhotoSize>? = null,
    @Optional @SerialName("delete_chat_photo") val deleteChatPhoto: Boolean? = null,
    @Optional @SerialName("group_chat_created") val groupChatCreated: Boolean? = null,
    @Optional @SerialName("supergroup_chat_created") val supergroupChatCreated: Boolean? = null,
    @Optional @SerialName("channel_chat_created") val channelChatCreated: Boolean? = null,
    @Optional @SerialName("migrate_to_chat_id") val migrateToChatId: Int? = null,
    @Optional @SerialName("migrate_from_chat_id") val migrateFromChatId: Int? = null,
    @Optional @SerialName("pinned_message") val pinnedMessage: Message? = null,
    @Optional val invoice: Invoice? = null,
    @Optional @SerialName("successful_payment") val successfulPayment: SuccessfulPayment? = null,
    @Optional @SerialName("connected_website") val connectedWebsite: String? = null,
    @Optional @SerialName("passport_data") val passportData: PassportData? = null
) : TextableTelegramEvent {
    @Transient    val contentType: ContentType by lazy {
        when {
            text != null -> ContentType.TEXT
            audio != null -> ContentType.AUDIO
            animation != null -> ContentType.ANIMATION
            document != null -> ContentType.DOCUMENT
            game != null -> ContentType.GAME
            photo != null -> ContentType.PHOTO
            sticker != null -> ContentType.STICKER
            video != null -> ContentType.VIDEO
            videoNote != null -> ContentType.VIDEO_NOTE
            voice != null -> ContentType.VOICE
            contact != null -> ContentType.CONTACT
            venue != null -> ContentType.VENUE
            location != null -> ContentType.LOCATION
            newChatMembers != null -> ContentType.NEW_CHAT_MEMBERS
            leftChatMember != null -> ContentType.LEFT_CHAT_MEMBER
            invoice != null -> ContentType.INVOICE
            successfulPayment != null -> ContentType.SUCCESSFUL_PAYMENT
            connectedWebsite != null -> ContentType.CONNECTED_WEBSITE
            migrateFromChatId != null -> ContentType.MIGRATE_FROM_CHAT_ID
            migrateToChatId != null -> ContentType.MIGRATE_TO_CHAT_ID
            pinnedMessage != null -> ContentType.PINNED_MESSAGE
            newChatTitle != null -> ContentType.NEW_CHAT_TITLE
            newChatPhoto != null -> ContentType.NEW_CHAT_PHOTO
            deleteChatPhoto != null -> ContentType.DELETE_CHAT_PHOTO
            groupChatCreated != null -> ContentType.GROUP_CHAT_CREATED
            passportData != null -> ContentType.PASSPORT_DATA
            else -> ContentType.UNKNOWN
        }
    }

    @Transient    override val eventText: String?
        get() = text ?: caption
}
