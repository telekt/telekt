package rocks.waffle.telekt.types

import kotlinx.serialization.Optional
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// `@Serializable`? (Will it work? Polimorphic serializer?...)
sealed class InlineQueryResult

sealed class InlineQueryResultCached : InlineQueryResult()

//<editor-fold desc="common">
/** Represents a link to a voice recording in an .ogg container encoded with OPUS. By default, this voice recording will be sent by the user. Alternatively, you can use input_message_content to send a message with the specified content instead of the the voice message. */
@Serializable data class InlineQueryResultVoice(
    val type: String,
    val id: String,
    @SerialName("voice_url") val voiceUrl: String,
    val title: String,
    @Optional val caption: String? = null,
    @Optional @SerialName("parse_mode") val parseMode: String? = null,
    @Optional @SerialName("voice_duration") val voiceDuration: Int? = null,
    @Optional @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null,
    @Optional @SerialName("input_message_content") val inputMessageContent: InputMessageContent? = null
) : InlineQueryResult()


/** Represents a link to a page containing an embedded video player or a video file. By default, this video file will be sent by the user with an optional caption. Alternatively, you can use input_message_content to send a message with the specified content instead of the video. If an InlineQueryResultVideo message contains an embedded video (e.g., YouTube), you must replace its content using input_message_content. */
@Serializable data class InlineQueryResultVideo(
    val type: String,
    val id: String,
    @SerialName("video_url") val videoUrl: String,
    @SerialName("mime_type") val mimeType: String,
    @SerialName("thumb_url") val thumbUrl: String,
    val title: String,
    @Optional val caption: String? = null,
    @Optional @SerialName("parse_mode") val parseMode: String? = null,
    @Optional @SerialName("video_width") val videoWidth: Int? = null,
    @Optional @SerialName("video_height") val videoHeight: Int? = null,
    @Optional @SerialName("video_duration") val videoDuration: Int? = null,
    @Optional val description: String? = null,
    @Optional @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null,
    @Optional @SerialName("input_message_content") val inputMessageContent: InputMessageContent? = null
) : InlineQueryResult()


/** Represents a venue. By default, the venue will be sent by the user. Alternatively, you can use input_message_content to send a message with the specified content instead of the venue. */
@Serializable data class InlineQueryResultVenue(
    val type: String,
    val id: String,
    val latitude: Float,
    val longitude: Float,
    val title: String,
    val address: String,
    @Optional @SerialName("foursquare_id") val foursquareId: String? = null,
    @Optional @SerialName("foursquare_type") val foursquareType: String? = null,
    @Optional @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null,
    @Optional @SerialName("input_message_content") val inputMessageContent: InputMessageContent? = null,
    @Optional @SerialName("thumb_url") val thumbUrl: String? = null,
    @Optional @SerialName("thumb_width") val thumbWidth: Int? = null,
    @Optional @SerialName("thumb_height") val thumbHeight: Int? = null
) : InlineQueryResult()


/** Represents a link to a photo. By default, this photo will be sent by the user with optional caption. Alternatively, you can use input_message_content to send a message with the specified content instead of the photo. */
@Serializable data class InlineQueryResultPhoto(
    val type: String,
    val id: String,
    @SerialName("photo_url") val photoUrl: String,
    @SerialName("thumb_url") val thumbUrl: String,
    @Optional @SerialName("photo_width") val photoWidth: Int? = null,
    @Optional @SerialName("photo_height") val photoHeight: Int? = null,
    @Optional val title: String? = null,
    @Optional val description: String? = null,
    @Optional val caption: String? = null,
    @Optional @SerialName("parse_mode") val parseMode: String? = null,
    @Optional @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null,
    @Optional @SerialName("input_message_content") val inputMessageContent: InputMessageContent? = null
) : InlineQueryResult()


/** Represents a link to a video animation (H.264/MPEG-4 AVC video without sound). By default, this animated MPEG-4 file will be sent by the user with optional caption. Alternatively, you can use input_message_content to send a message with the specified content instead of the animation. */
@Serializable data class InlineQueryResultMpeg4Gif(
    val type: String,
    val id: String,
    @SerialName("mpeg4_url") val mpeg4Url: String,
    @Optional @SerialName("mpeg4_width") val mpeg4Width: Int? = null,
    @Optional @SerialName("mpeg4_height") val mpeg4Height: Int? = null,
    @Optional @SerialName("mpeg4_duration") val mpeg4Duration: Int? = null,
    @SerialName("thumb_url") val thumbUrl: String,
    @Optional val title: String? = null,
    @Optional val caption: String? = null,
    @Optional @SerialName("parse_mode") val parseMode: String? = null,
    @Optional @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null,
    @Optional @SerialName("input_message_content") val inputMessageContent: InputMessageContent? = null
) : InlineQueryResult()


/** Represents a location on a map. By default, the location will be sent by the user. Alternatively, you can use input_message_content to send a message with the specified content instead of the location. */
@Serializable data class InlineQueryResultLocation(
    val type: String,
    val id: String,
    val latitude: Float,
    val longitude: Float,
    val title: String,
    @Optional @SerialName("live_period") val livePeriod: Int? = null,
    @Optional @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null,
    @Optional @SerialName("input_message_content") val inputMessageContent: InputMessageContent? = null,
    @Optional @SerialName("thumb_url") val thumbUrl: String? = null,
    @Optional @SerialName("thumb_width") val thumbWidth: Int? = null,
    @Optional @SerialName("thumb_height") val thumbHeight: Int? = null
) : InlineQueryResult()


/** Represents a link to an animated GIF file. By default, this animated GIF file will be sent by the user with optional caption. Alternatively, you can use input_message_content to send a message with the specified content instead of the animation. */
@Serializable data class InlineQueryResultGif(
    val type: String,
    val id: String,
    @SerialName("gif_url") val gifUrl: String,
    @Optional @SerialName("gif_width") val gifWidth: Int? = null,
    @Optional @SerialName("gif_height") val gifHeight: Int? = null,
    @Optional @SerialName("gif_duration") val gifDuration: Int? = null,
    @SerialName("thumb_url") val thumbUrl: String,
    @Optional val title: String? = null,
    @Optional val caption: String? = null,
    @Optional @SerialName("parse_mode") val parseMode: String? = null,
    @Optional @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null,
    @Optional @SerialName("input_message_content") val inputMessageContent: InputMessageContent? = null
) : InlineQueryResult()


/** Represents a link to a file. By default, this file will be sent by the user with an optional caption. Alternatively, you can use input_message_content to send a message with the specified content instead of the file. Currently, only .PDF and .ZIP files can be sent using this method. */
@Serializable data class InlineQueryResultDocument(
    val type: String,
    val id: String,
    val title: String,
    @Optional val caption: String? = null,
    @Optional @SerialName("parse_mode") val parseMode: String? = null,
    @SerialName("document_url") val documentUrl: String,
    @SerialName("mime_type") val mimeType: String,
    @Optional val description: String? = null,
    @Optional @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null,
    @Optional @SerialName("input_message_content") val inputMessageContent: InputMessageContent? = null,
    @Optional @SerialName("thumb_url") val thumbUrl: String? = null,
    @Optional @SerialName("thumb_width") val thumbWidth: Int? = null,
    @Optional @SerialName("thumb_height") val thumbHeight: Int? = null
) : InlineQueryResult()


/** Represents a Game. */
@Serializable data class InlineQueryResultGame(
    val type: String,
    val id: String,
    @SerialName("game_short_name") val gameShortName: String,
    @Optional @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null
) : InlineQueryResult()


/** Represents a contact with a phone number. By default, this contact will be sent by the user. Alternatively, you can use input_message_content to send a message with the specified content instead of the contact. */
@Serializable data class InlineQueryResultContact(
    val type: String,
    val id: String,
    @SerialName("phone_number") val phoneNumber: String,
    @SerialName("first_name") val firstName: String,
    @Optional @SerialName("last_name") val lastName: String? = null,
    @Optional val vcard: String? = null,
    @Optional @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null,
    @Optional @SerialName("input_message_content") val inputMessageContent: InputMessageContent? = null,
    @Optional @SerialName("thumb_url") val thumbUrl: String? = null,
    @Optional @SerialName("thumb_width") val thumbWidth: Int? = null,
    @Optional @SerialName("thumb_height") val thumbHeight: Int? = null
) : InlineQueryResult()


/** Represents a link to an mp3 audio file. By default, this audio file will be sent by the user. Alternatively, you can use input_message_content to send a message with the specified content instead of the audio. */
@Serializable data class InlineQueryResultAudio(
    val type: String,
    val id: String,
    @SerialName("audio_url") val audioUrl: String,
    val title: String,
    @Optional val caption: String? = null,
    @Optional @SerialName("parse_mode") val parseMode: String? = null,
    @Optional val performer: String? = null,
    @Optional @SerialName("audio_duration") val audioDuration: Int? = null,
    @Optional @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null,
    @Optional @SerialName("input_message_content") val inputMessageContent: InputMessageContent? = null
) : InlineQueryResult()


/** Represents a link to an article or web page. */
@Serializable data class InlineQueryResultArticle(
    val type: String,
    val id: String,
    val title: String,
    @SerialName("input_message_content") val inputMessageContent: InputMessageContent,
    @Optional @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null,
    @Optional val url: String? = null,
    @Optional @SerialName("hide_url") val hideUrl: Boolean? = null,
    @Optional val description: String? = null,
    @Optional @SerialName("thumb_url") val thumbUrl: String? = null,
    @Optional @SerialName("thumb_width") val thumbWidth: Int? = null,
    @Optional @SerialName("thumb_height") val thumbHeight: Int? = null
) : InlineQueryResult()
//</editor-fold>

//<editor-fold desc="cashed">
/** Represents a link to a video file stored on the Telegram servers. By default, this video file will be sent by the user with an optional caption. Alternatively, you can use input_message_content to send a message with the specified content instead of the video. */
@Serializable data class InlineQueryResultCachedVideo(
    val type: String,
    val id: String,
    @SerialName("video_file_id") val videoFileId: String,
    val title: String,
    @Optional val description: String? = null,
    @Optional val caption: String? = null,
    @Optional @SerialName("parse_mode") val parseMode: String? = null,
    @Optional @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null,
    @Optional @SerialName("input_message_content") val inputMessageContent: InputMessageContent? = null
) : InlineQueryResultCached()


/** Represents a link to a sticker stored on the Telegram servers. By default, this sticker will be sent by the user. Alternatively, you can use input_message_content to send a message with the specified content instead of the sticker. */
@Serializable data class InlineQueryResultCachedSticker(
    val type: String,
    val id: String,
    @SerialName("sticker_file_id") val stickerFileId: String,
    @Optional @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null,
    @Optional @SerialName("input_message_content") val inputMessageContent: InputMessageContent? = null
) : InlineQueryResultCached()


/** Represents a link to a photo stored on the Telegram servers. By default, this photo will be sent by the user with an optional caption. Alternatively, you can use input_message_content to send a message with the specified content instead of the photo. */
@Serializable data class InlineQueryResultCachedPhoto(
    val type: String,
    val id: String,
    @SerialName("photo_file_id") val photoFileId: String,
    @Optional val title: String? = null,
    @Optional val description: String? = null,
    @Optional val caption: String? = null,
    @Optional @SerialName("parse_mode") val parseMode: String? = null,
    @Optional @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null,
    @Optional @SerialName("input_message_content") val inputMessageContent: InputMessageContent? = null
) : InlineQueryResultCached()


/** Represents a link to a video animation (H.264/MPEG-4 AVC video without sound) stored on the Telegram servers. By default, this animated MPEG-4 file will be sent by the user with an optional caption. Alternatively, you can use input_message_content to send a message with the specified content instead of the animation. */
@Serializable data class InlineQueryResultCachedMpeg4Gif(
    val type: String,
    val id: String,
    @SerialName("mpeg4_file_id") val mpeg4FileId: String,
    @Optional val title: String? = null,
    @Optional val caption: String? = null,
    @Optional @SerialName("parse_mode") val parseMode: String? = null,
    @Optional @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null,
    @Optional @SerialName("input_message_content") val inputMessageContent: InputMessageContent? = null
) : InlineQueryResultCached()


/** Represents a link to an animated GIF file stored on the Telegram servers. By default, this animated GIF file will be sent by the user with an optional caption. Alternatively, you can use input_message_content to send a message with specified content instead of the animation. */
@Serializable data class InlineQueryResultCachedGif(
    val type: String,
    val id: String,
    @SerialName("gif_file_id") val gifFileId: String,
    @Optional val title: String? = null,
    @Optional val caption: String? = null,
    @Optional @SerialName("parse_mode") val parseMode: String? = null,
    @Optional @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null,
    @Optional @SerialName("input_message_content") val inputMessageContent: InputMessageContent? = null
) : InlineQueryResultCached()


/** Represents a link to a file stored on the Telegram servers. By default, this file will be sent by the user with an optional caption. Alternatively, you can use input_message_content to send a message with the specified content instead of the file. */
@Serializable data class InlineQueryResultCachedDocument(
    val type: String,
    val id: String,
    val title: String,
    @SerialName("document_file_id") val documentFileId: String,
    @Optional val description: String? = null,
    @Optional val caption: String? = null,
    @Optional @SerialName("parse_mode") val parseMode: String? = null,
    @Optional @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null,
    @Optional @SerialName("input_message_content") val inputMessageContent: InputMessageContent? = null
) : InlineQueryResultCached()


/** Represents a link to an mp3 audio file stored on the Telegram servers. By default, this audio file will be sent by the user. Alternatively, you can use input_message_content to send a message with the specified content instead of the audio. */
@Serializable data class InlineQueryResultCachedAudio(
    val type: String,
    val id: String,
    @SerialName("audio_file_id") val audioFileId: String,
    @Optional val caption: String? = null,
    @Optional @SerialName("parse_mode") val parseMode: String? = null,
    @Optional @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null,
    @Optional @SerialName("input_message_content") val inputMessageContent: InputMessageContent? = null
) : InlineQueryResultCached()


/** Represents a link to a voice message stored on the Telegram servers. By default, this voice message will be sent by the user. Alternatively, you can use input_message_content to send a message with the specified content instead of the voice message. */
@Serializable data class InlineQueryResultCachedVoice(
    val type: String,
    val id: String,
    @SerialName("voice_file_id") val voiceFileId: String,
    val title: String,
    @Optional val caption: String? = null,
    @Optional @SerialName("parse_mode") val parseMode: String? = null,
    @Optional @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null,
    @Optional @SerialName("input_message_content") val inputMessageContent: InputMessageContent? = null
) : InlineQueryResultCached()
//</editor-fold>