package telekt.types

import kotlinx.serialization.*
import telekt.network.InputFile
import telekt.util.enumByValueSerializer

@Serializable(with = InputMediaType.S::class) enum class InputMediaType(val apiName: String) {
    ANIMATION("animation"),
    DOCUMENT("document"),
    AUDIO("audio"),
    PHOTO("photo"),
    VIDEO("video");

    @Serializer(forClass = InputMediaType::class)
    object S : KSerializer<InputMediaType> by enumByValueSerializer({ apiName })
}


@Serializable(with = InputMediaSerializer::class) sealed class InputMedia {
    abstract val type: InputMediaType
    abstract val media: InputFile
    abstract val caption: String?

    @Transient abstract val files: List<InputFile>
}

@Serializer(forClass = InputMedia::class) object InputMediaSerializer : KSerializer<InputMedia> {
    override fun deserialize(decoder: Decoder): InputMedia {
        throw NotImplementedError()
    }

    override fun serialize(encoder: Encoder, obj: InputMedia) {
        return when (obj) {
            is InputMediaAnimation -> InputMediaAnimation.serializer().serialize(encoder, obj)
            is InputMediaAudio -> InputMediaAudio.serializer().serialize(encoder, obj)
            is InputMediaDocument -> InputMediaDocument.serializer().serialize(encoder, obj)
            is InputMediaPhoto -> InputMediaPhoto.serializer().serialize(encoder, obj)
            is InputMediaVideo -> InputMediaVideo.serializer().serialize(encoder, obj)
        }
    }
}

/** Represents an animation file (GIF or H.264/MPEG-4 AVC video without sound) to be sent. */
@Serializable data class InputMediaAnimation(
    override val media: InputFile = throw RuntimeException("InputFile has not default value") /* dirty workaround */,
    @Optional val thumb: InputFile? = null,
    @Optional override val caption: String? = null,
    @Optional @SerialName("parse_mode") val parseMode: String? = null,
    @Optional val width: Int? = null,
    @Optional val height: Int? = null,
    @Optional val duration: Int? = null
) : InputMedia() {
    override val type: InputMediaType = InputMediaType.ANIMATION
    @Transient override val files: List<InputFile> = if (thumb != null) listOf(media, thumb) else listOf(media)
}

/** Represents an audio file to be treated as music to be sent. */
@Serializable data class InputMediaAudio(
    override val media: InputFile = throw RuntimeException("InputFile has not default value") /* dirty workaround */,
    @Optional val thumb: InputFile? = null,
    @Optional override val caption: String? = null,
    @Optional @SerialName("parse_mode") val parseMode: String? = null,
    @Optional val duration: Int? = null,
    @Optional val performer: String? = null,
    @Optional val title: String? = null
) : InputMedia() {
    override val type: InputMediaType = InputMediaType.AUDIO
    @Transient    override val files: List<InputFile> = if (thumb != null) listOf(media, thumb) else listOf(media)
}

/** Represents a general file to be sent. */
@Serializable data class InputMediaDocument(
    override val media: InputFile = throw RuntimeException("InputFile has not default value") /* dirty workaround */,
    @Optional val thumb: InputFile? = null,
    @Optional override val caption: String? = null,
    @Optional @SerialName("parse_mode") val parseMode: String? = null
) : InputMedia() {
    override val type: InputMediaType = InputMediaType.DOCUMENT
    @Transient    override val files: List<InputFile> = if (thumb != null) listOf(media, thumb) else listOf(media)
}

/** Represents a photo to be sent. */
@Serializable data class InputMediaPhoto(
    override val media: InputFile = throw RuntimeException("InputFile has not default value") /* dirty workaround */,
    @Optional override val caption: String? = null,
    @Optional @SerialName("parse_mode") val parseMode: String? = null
) : InputMedia() {
    override val type: InputMediaType = InputMediaType.PHOTO
    @Transient    override val files: List<InputFile> = listOf(media)
}

/** Represents a video to be sent. */
@Serializable data class InputMediaVideo(
    override val media: InputFile = throw RuntimeException("InputFile has not default value") /* dirty workaround */,
    @Optional val thumb: InputFile? = null,
    @Optional override val caption: String? = null,
    @Optional @SerialName("parse_mode") val parseMode: String? = null,
    @Optional val width: Int? = null,
    @Optional val height: Int? = null,
    @Optional val duration: Int? = null,
    @Optional @SerialName("supports_streaming") val supportsStreaming: Boolean? = null
) : InputMedia() {
    override val type: InputMediaType = InputMediaType.ANIMATION
    @Transient    override val files: List<InputFile> = if (thumb != null) listOf(media, thumb) else listOf(media)
}
