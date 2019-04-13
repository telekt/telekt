package telekt.types.enums

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import telekt.util.enumByValueSerializer

@Serializable(with = ChatAction.S::class)
enum class ChatAction(val apiName: String) {
    TYPING("typing"),
    UPLOAD_PHOTO("upload_photo"),
    RECORD_VIDEO("record_video"),
    UPLOAD_VIDEO("upload_video"),
    RECORD_AUDIO("record_audio"),
    UPLOAD_AUDIO("upload_audio"),
    UPLOAD_DOCUMENT("upload_document"),
    FIND_LOCATION("find_location");

    @Serializer(forClass = ChatAction::class)
    object S : KSerializer<ChatAction> by enumByValueSerializer({ apiName })
}