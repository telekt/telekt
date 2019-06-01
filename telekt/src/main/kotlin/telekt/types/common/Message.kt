package rocks.waffle.telekt.types

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import rocks.waffle.telekt.contrib.filters.TextableTelegramEvent
import rocks.waffle.telekt.types.enums.ContentType
import rocks.waffle.telekt.types.passport.PassportData
import rocks.waffle.telekt.types.replymarkup.ReplyMarkup


/** This object represents a message. */
@Serializable data class Message(
    @SerialName("message_id") val messageId: Int,
    val date: Int,
    val chat: Chat,
    val from: User? = null,
    @SerialName("forward_from") val forwardFrom: User? = null,
    @SerialName("forward_from_chat") val forwardFromChat: Chat? = null,
    @SerialName("forward_from_message_id") val forwardFromMessageId: Int? = null,
    @SerialName("forward_signature") val forwardSignature: String? = null,
    @SerialName("forward_sender_name") val forwardSenderName: String? = null,
    @SerialName("forward_date") val forwardDate: Int? = null,
    @SerialName("reply_to_message") val replyToMessage: Message? = null,
    @SerialName("edit_date") val editDate: Int? = null,
    @SerialName("media_group_id") val mediaGroupId: String? = null,
    @SerialName("author_signature") val authorSignature: String? = null,
    val text: String? = null,
    val entities: Array<MessageEntity>? = null,
    @SerialName("caption_entities") val captionEntities: Array<MessageEntity>? = null,
    val audio: Audio? = null,
    val document: Document? = null,
    val animation: Animation? = null,
    val game: Game? = null,
    val photo: Array<PhotoSize>? = null,
    val sticker: Sticker? = null,
    val video: Video? = null,
    val voice: Voice? = null,
    @SerialName("video_note") val videoNote: VideoNote? = null,
    val caption: String? = null,
    val contact: Contact? = null,
    val location: Location? = null,
    val venue: Venue? = null,
    val poll: Poll? = null,
    @SerialName("new_chat_members") val newChatMembers: Array<User>? = null,
    @SerialName("left_chat_member") val leftChatMember: User? = null,
    @SerialName("new_chat_title") val newChatTitle: String? = null,
    @SerialName("new_chat_photo") val newChatPhoto: Array<PhotoSize>? = null,
    @SerialName("delete_chat_photo") val deleteChatPhoto: Boolean? = null,
    @SerialName("group_chat_created") val groupChatCreated: Boolean? = null,
    @SerialName("supergroup_chat_created") val supergroupChatCreated: Boolean? = null,
    @SerialName("channel_chat_created") val channelChatCreated: Boolean? = null,
    @SerialName("migrate_to_chat_id") val migrateToChatId: Int? = null,
    @SerialName("migrate_from_chat_id") val migrateFromChatId: Int? = null,
    @SerialName("pinned_message") val pinnedMessage: Message? = null,
    val invoice: Invoice? = null,
    @SerialName("successful_payment") val successfulPayment: SuccessfulPayment? = null,
    @SerialName("connected_website") val connectedWebsite: String? = null,
    @SerialName("passport_data") val passportData: PassportData? = null,
    @SerialName("reply_markup") val replyMarkup: ReplyMarkup? = null
) : TextableTelegramEvent {
    @Transient val contentType: ContentType by lazy {
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
            poll != null -> ContentType.POLL
            else -> ContentType.UNKNOWN
        }
    }

    @Transient override val eventText: String?
        get() = text ?: caption
}
