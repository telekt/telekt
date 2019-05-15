package rocks.waffle.telekt.bot

import io.ktor.client.HttpClient
import kotlinx.coroutines.Deferred
import rocks.waffle.telekt.network.Api
import rocks.waffle.telekt.network.DefaultApi
import rocks.waffle.telekt.network.InputFile
import rocks.waffle.telekt.network.requests.edit.StopMessageLiveLocation
import rocks.waffle.telekt.network.requests.edit.StopMessageLiveLocationInline
import rocks.waffle.telekt.types.*
import rocks.waffle.telekt.types.enums.AllowedUpdate
import rocks.waffle.telekt.types.enums.ParseMode
import rocks.waffle.telekt.types.passport.PassportElementError
import rocks.waffle.telekt.util.Recipient
import java.nio.file.Path
import java.nio.file.Paths

/** Bot factory */
@Suppress("FunctionName")
fun Bot(token: String, client: HttpClient? = null, api: Api = DefaultApi, defaultParseMode: ParseMode? = null): Bot =
    BotImpl(token, client = client, api = api, defaultParseMode = defaultParseMode)

suspend fun Bot.downloadFile(path: String, destination: String): Unit = downloadFile(path, Paths.get(destination))

suspend fun Bot.downloadFileByFileId(fileId: String, destination: String): Unit {
    val path = getFile(fileId).filePath!!
    downloadFile(path, destination)
}

suspend fun Bot.downloadFileByFileId(fileId: String, destination: Path): Unit {
    val path = getFile(fileId).filePath!!
    downloadFile(path, destination)
}

interface Bot {
    val me: Deferred<User>

    suspend fun downloadFile(path: String, destination: Path)

    //<editor-fold desc="api">
    /**
     * A simple method for testing your bot's auth token. Requires no parameters.
     * Returns basic information about the bot in form of a [User] object.
     *
     * More: https://core.telegram.org/bots/api#getme
     */
    suspend fun getMe(): User

    /**
     * Use this method to receive incoming updates using long polling (wiki)[https://en.wikipedia.org/wiki/Push_technology#Long_polling].
     * An Array of [Update] objects is returned.
     *
     * Notes
     *   1. This method will not work if an outgoing webhook is set up.
     *   2. In order to avoid getting duplicate updates, recalculate offset after each server response.
     *
     * @param offset Identifier of the first update to be returned.
     * Must be greater by one than the highest among the identifiers of previously received updates.
     * By default, updates starting with the earliest unconfirmed update are returned.
     * An update is considered confirmed as soon as getUpdates is called with an offset higher than its update_id.
     * The negative offset can be specified to retrieve updates starting from -offset update from the end of the updates queue.
     * All previous updates will forgotten.
     *
     * @param limit Limits the number of updates to be retrieved. Values between 1—100 are accepted. Defaults to 100.
     *
     * @param timeout Timeout in seconds for long polling. Defaults to 0, i.e. usual short polling.
     * Should be positive, short polling should be used for testing purposes only.
     *
     * @param allowedUpdates List the types of updates you want your bot to receive.
     * For example, specify [“message”, “edited_channel_post”, “callback_query”] to only receive updates of these types.
     * See [Update] for a complete list of available update types.
     * Specify an empty list to receive all updates regardless of type (default).
     * If not specified, the previous setting will be used.
     * Please note that this parameter doesn't affect updates created before the call to the getUpdates,
     * so unwanted updates may be received for a short period of time.
     *
     * More: https://core.telegram.org/bots/api#getupdates
     */
    suspend fun getUpdates(
        offset: Int? = null,
        limit: Byte? = null,
        timeout: Int? = null,
        allowedUpdates: List<AllowedUpdate>? = null
    ): List<Update>

    /**
     * Use this method to delete a message, including service messages, with the following limitations:
     *   - A message can only be deleted if it was sent less than 48 hours ago.
     *   - Bots can delete outgoing messages in private chats, groups, and supergroups.
     *   - Bots can delete incoming messages in private chats.
     *   - Bots granted can_post_messages permissions can delete outgoing messages in channels.
     *   - If the bot is an administrator of a group, it can delete any message there.
     *   - If the bot has can_delete_messages permission in a supergroup or a channel, it can delete any message there.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     * @param messageId Identifier of the message to delete
     */
    suspend fun deleteMessage(
        chatId: Recipient,
        messageId: Int
    ): Unit

    //<editor-fold desc="auto">
    /**
     * Use this method to send text messages. On success, the sent Message is returned.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     * @param text Text of the message to be sent
     * @param parseMode Send Markdown or HTML, if you want Telegram apps to show bold, italic, fixed-width text or inline URLs in your bot's message.
     * @param disableWebPagePreview Disables link previews for links in this message
     * @param disableNotification Sends the message silently. Users will receive a notification with no sound.
     * @param replyToMessageId If the message is a reply, ID of the original message
     * @param replyMarkup Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard, instructions to remove reply keyboard or to force a reply from the user.
     *
     * more: https://core.telegram.org/bots/api#sendmessage
     */
    suspend fun sendMessage(
        chatId: Recipient,
        text: String,
        parseMode: ParseMode? = null,
        disableWebPagePreview: Boolean? = null,
        disableNotification: Boolean? = null,
        replyToMessageId: Int? = null,
        replyMarkup: ReplyMarkup? = null
    ): Message

    /**
     * Use this method to forward messages of any kind. On success, the sent Message is returned.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     * @param fromChatId Unique identifier for the chat where the original message was sent (or channel username in the format @channelusername)
     * @param disableNotification Sends the message silently. Users will receive a notification with no sound.
     * @param messageId Message identifier in the chat specified in from_chat_id
     *
     * more: https://core.telegram.org/bots/api#forwardmessage
     */
    suspend fun forwardMessage(
        chatId: Recipient,
        fromChatId: Recipient,
        disableNotification: Boolean? = null,
        messageId: Int
    ): Message

    /**
     * Use this method to send photos. On success, the sent Message is returned.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     * @param photo Photo to send. Pass a file_id as String to send a photo that exists on the Telegram servers (recommended), pass an HTTP URL as a String for Telegram to get a photo from the Internet, or upload a new photo using multipart/form-data. More info on Sending Files »
     * @param caption Photo caption (may also be used when resending photos by file_id), 0-1024 characters
     * @param parseMode Send Markdown or HTML, if you want Telegram apps to show bold, italic, fixed-width text or inline URLs in the media caption.
     * @param disableNotification Sends the message silently. Users will receive a notification with no sound.
     * @param replyToMessageId If the message is a reply, ID of the original message
     * @param replyMarkup Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard, instructions to remove reply keyboard or to force a reply from the user.
     *
     * more: https://core.telegram.org/bots/api#sendphoto
     */
    suspend fun sendPhoto(
        chatId: Recipient,
        photo: InputFile,
        caption: String? = null,
        parseMode: ParseMode? = null,
        disableNotification: Boolean? = null,
        replyToMessageId: Int? = null,
        replyMarkup: ReplyMarkup? = null
    ): Message

    /**
     * Use this method to send audio files, if you want Telegram clients to display them in the music player. Your audio must be in the .mp3 format. On success, the sent Message is returned. Bots can currently send audio files of up to 50 MB in size, this limit may be changed in the future. For sending voice messages, use the sendVoice method instead.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     * @param audio Audio file to send. Pass a file_id as String to send an audio file that exists on the Telegram servers (recommended), pass an HTTP URL as a String for Telegram to get an audio file from the Internet, or upload a new one using multipart/form-data. More info on Sending Files »
     * @param caption Audio caption, 0-1024 characters
     * @param parseMode Send Markdown or HTML, if you want Telegram apps to show bold, italic, fixed-width text or inline URLs in the media caption.
     * @param duration Duration of the audio in seconds
     * @param performer Performer
     * @param title Track name
     * @param thumb Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported server-side. The thumbnail should be in JPEG format and less than 200 kB in size. A thumbnail‘s width and height should not exceed 90. Ignored if the file is not uploaded using multipart/form-data. Thumbnails can’t be reused and can be only uploaded as a new file, so you can pass “attach://<file_attach_name>” if the thumbnail was uploaded using multipart/form-data under <file_attach_name>. More info on Sending Files »
     * @param disableNotification Sends the message silently. Users will receive a notification with no sound.
     * @param replyToMessageId If the message is a reply, ID of the original message
     * @param replyMarkup Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard, instructions to remove reply keyboard or to force a reply from the user.
     *
     * more: https://core.telegram.org/bots/api#sendaudio
     */
    suspend fun sendAudio(
        chatId: Recipient,
        audio: InputFile,
        caption: String? = null,
        parseMode: ParseMode? = null,
        duration: Int? = null,
        performer: String? = null,
        title: String? = null,
        thumb: InputFile? = null,
        disableNotification: Boolean? = null,
        replyToMessageId: Int? = null,
        replyMarkup: ReplyMarkup? = null
    ): Message

    /**
     * Use this method to send general files. On success, the sent Message is returned. Bots can currently send files of any type of up to 50 MB in size, this limit may be changed in the future.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     * @param document File to send. Pass a file_id as String to send a file that exists on the Telegram servers (recommended), pass an HTTP URL as a String for Telegram to get a file from the Internet, or upload a new one using multipart/form-data. More info on Sending Files »
     * @param thumb Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported server-side. The thumbnail should be in JPEG format and less than 200 kB in size. A thumbnail‘s width and height should not exceed 90. Ignored if the file is not uploaded using multipart/form-data. Thumbnails can’t be reused and can be only uploaded as a new file, so you can pass “attach://<file_attach_name>” if the thumbnail was uploaded using multipart/form-data under <file_attach_name>. More info on Sending Files »
     * @param caption Document caption (may also be used when resending documents by file_id), 0-1024 characters
     * @param parseMode Send Markdown or HTML, if you want Telegram apps to show bold, italic, fixed-width text or inline URLs in the media caption.
     * @param disableNotification Sends the message silently. Users will receive a notification with no sound.
     * @param replyToMessageId If the message is a reply, ID of the original message
     * @param replyMarkup Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard, instructions to remove reply keyboard or to force a reply from the user.
     *
     * more: https://core.telegram.org/bots/api#senddocument
     */
    suspend fun sendDocument(
        chatId: Recipient,
        document: InputFile,
        thumb: InputFile? = null,
        caption: String? = null,
        parseMode: ParseMode? = null,
        disableNotification: Boolean? = null,
        replyToMessageId: Int? = null,
        replyMarkup: ReplyMarkup? = null
    ): Message

    /**
     * Use this method to send video files, Telegram clients support mp4 videos (other formats may be sent as Document). On success, the sent Message is returned. Bots can currently send video files of up to 50 MB in size, this limit may be changed in the future.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     * @param video Video to send. Pass a file_id as String to send a video that exists on the Telegram servers (recommended), pass an HTTP URL as a String for Telegram to get a video from the Internet, or upload a new video using multipart/form-data. More info on Sending Files »
     * @param duration Duration of sent video in seconds
     * @param width Video width
     * @param height Video height
     * @param thumb Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported server-side. The thumbnail should be in JPEG format and less than 200 kB in size. A thumbnail‘s width and height should not exceed 90. Ignored if the file is not uploaded using multipart/form-data. Thumbnails can’t be reused and can be only uploaded as a new file, so you can pass “attach://<file_attach_name>” if the thumbnail was uploaded using multipart/form-data under <file_attach_name>. More info on Sending Files »
     * @param caption Video caption (may also be used when resending videos by file_id), 0-1024 characters
     * @param parseMode Send Markdown or HTML, if you want Telegram apps to show bold, italic, fixed-width text or inline URLs in the media caption.
     * @param supportsStreaming Pass True, if the uploaded video is suitable for streaming
     * @param disableNotification Sends the message silently. Users will receive a notification with no sound.
     * @param replyToMessageId If the message is a reply, ID of the original message
     * @param replyMarkup Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard, instructions to remove reply keyboard or to force a reply from the user.
     *
     * more: https://core.telegram.org/bots/api#sendvideo
     */
    suspend fun sendVideo(
        chatId: Recipient,
        video: InputFile,
        duration: Int? = null,
        width: Int? = null,
        height: Int? = null,
        thumb: InputFile? = null,
        caption: String? = null,
        parseMode: ParseMode? = null,
        supportsStreaming: Boolean? = null,
        disableNotification: Boolean? = null,
        replyToMessageId: Int? = null,
        replyMarkup: ReplyMarkup? = null
    ): Message

    /**
     * Use this method to send animation files (GIF or H.264/MPEG-4 AVC video without sound). On success, the sent Message is returned. Bots can currently send animation files of up to 50 MB in size, this limit may be changed in the future.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     * @param animation Animation to send. Pass a file_id as String to send an animation that exists on the Telegram servers (recommended), pass an HTTP URL as a String for Telegram to get an animation from the Internet, or upload a new animation using multipart/form-data. More info on Sending Files »
     * @param duration Duration of sent animation in seconds
     * @param width Animation width
     * @param height Animation height
     * @param thumb Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported server-side. The thumbnail should be in JPEG format and less than 200 kB in size. A thumbnail‘s width and height should not exceed 90. Ignored if the file is not uploaded using multipart/form-data. Thumbnails can’t be reused and can be only uploaded as a new file, so you can pass “attach://<file_attach_name>” if the thumbnail was uploaded using multipart/form-data under <file_attach_name>. More info on Sending Files »
     * @param caption Animation caption (may also be used when resending animation by file_id), 0-1024 characters
     * @param parseMode Send Markdown or HTML, if you want Telegram apps to show bold, italic, fixed-width text or inline URLs in the media caption.
     * @param disableNotification Sends the message silently. Users will receive a notification with no sound.
     * @param replyToMessageId If the message is a reply, ID of the original message
     * @param replyMarkup Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard, instructions to remove reply keyboard or to force a reply from the user.
     *
     * more: https://core.telegram.org/bots/api#sendanimation
     */
    suspend fun sendAnimation(
        chatId: Recipient,
        animation: InputFile,
        duration: Int? = null,
        width: Int? = null,
        height: Int? = null,
        thumb: InputFile? = null,
        caption: String? = null,
        parseMode: ParseMode? = null,
        disableNotification: Boolean? = null,
        replyToMessageId: Int? = null,
        replyMarkup: ReplyMarkup? = null
    ): Message

    /**
     * Use this method to send audio files, if you want Telegram clients to display the file as a playable voice message. For this to work, your audio must be in an .ogg file encoded with OPUS (other formats may be sent as Audio or Document). On success, the sent Message is returned. Bots can currently send voice messages of up to 50 MB in size, this limit may be changed in the future.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     * @param voice Audio file to send. Pass a file_id as String to send a file that exists on the Telegram servers (recommended), pass an HTTP URL as a String for Telegram to get a file from the Internet, or upload a new one using multipart/form-data. More info on Sending Files »
     * @param caption Voice message caption, 0-1024 characters
     * @param parseMode Send Markdown or HTML, if you want Telegram apps to show bold, italic, fixed-width text or inline URLs in the media caption.
     * @param duration Duration of the voice message in seconds
     * @param disableNotification Sends the message silently. Users will receive a notification with no sound.
     * @param replyToMessageId If the message is a reply, ID of the original message
     * @param replyMarkup Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard, instructions to remove reply keyboard or to force a reply from the user.
     *
     * more: https://core.telegram.org/bots/api#sendvoice
     */
    suspend fun sendVoice(
        chatId: Recipient,
        voice: InputFile,
        caption: String? = null,
        parseMode: ParseMode? = null,
        duration: Int? = null,
        disableNotification: Boolean? = null,
        replyToMessageId: Int? = null,
        replyMarkup: ReplyMarkup? = null
    ): Message

    /**
     * As of v.4.0, Telegram clients support rounded square mp4 videos of up to 1 minute long. Use this method to send video messages. On success, the sent Message is returned.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     * @param videoNote Video note to send. Pass a file_id as String to send a video note that exists on the Telegram servers (recommended) or upload a new video using multipart/form-data. More info on Sending Files ». Sending video notes by a URL is currently unsupported
     * @param duration Duration of sent video in seconds
     * @param length Video width and height, i.e. diameter of the video message
     * @param thumb Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported server-side. The thumbnail should be in JPEG format and less than 200 kB in size. A thumbnail‘s width and height should not exceed 90. Ignored if the file is not uploaded using multipart/form-data. Thumbnails can’t be reused and can be only uploaded as a new file, so you can pass “attach://<file_attach_name>” if the thumbnail was uploaded using multipart/form-data under <file_attach_name>. More info on Sending Files »
     * @param disableNotification Sends the message silently. Users will receive a notification with no sound.
     * @param replyToMessageId If the message is a reply, ID of the original message
     * @param replyMarkup Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard, instructions to remove reply keyboard or to force a reply from the user.
     *
     * more: https://core.telegram.org/bots/api#sendvideonote
     */
    suspend fun sendVideoNote(
        chatId: Recipient,
        videoNote: InputFile,
        duration: Int? = null,
        length: Int? = null,
        thumb: InputFile? = null,
        disableNotification: Boolean? = null,
        replyToMessageId: Int? = null,
        replyMarkup: ReplyMarkup? = null
    ): Message

    /**
     * Use this method to send a group of photos or videos as an album. On success, an array of the sent Messages is returned.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     * @param media A JSON-serialized array describing photos and videos to be sent, must include 2–10 items
     * @param disableNotification Sends the messages silently. Users will receive a notification with no sound.
     * @param replyToMessageId If the messages are a reply, ID of the original message
     *
     * more: https://core.telegram.org/bots/api#sendmediagroup
     */
    suspend fun sendMediaGroup(
        chatId: Recipient,
        media: List<InputMedia>,
        disableNotification: Boolean? = null,
        replyToMessageId: Int? = null
    ): List<Message>

    /**
     * Use this method to send point on the map. On success, the sent Message is returned.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     * @param latitude Latitude of the location
     * @param longitude Longitude of the location
     * @param livePeriod Period in seconds for which the location will be updated (see Live Locations, should be between 60 and 86400.
     * @param disableNotification Sends the message silently. Users will receive a notification with no sound.
     * @param replyToMessageId If the message is a reply, ID of the original message
     * @param replyMarkup Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard, instructions to remove reply keyboard or to force a reply from the user.
     *
     * more: https://core.telegram.org/bots/api#sendlocation
     */
    suspend fun sendLocation(
        chatId: Recipient,
        latitude: Float,
        longitude: Float,
        livePeriod: Int? = null,
        disableNotification: Boolean? = null,
        replyToMessageId: Int? = null,
        replyMarkup: ReplyMarkup? = null
    ): Message

    /**
     * Use this method to send information about a venue. On success, the sent Message is returned.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     * @param latitude Latitude of the venue
     * @param longitude Longitude of the venue
     * @param title Name of the venue
     * @param address Address of the venue
     * @param foursquareId Foursquare identifier of the venue
     * @param foursquareType Foursquare type of the venue, if known. (For example, “arts_entertainment/default”, “arts_entertainment/aquarium” or “food/icecream”.)
     * @param disableNotification Sends the message silently. Users will receive a notification with no sound.
     * @param replyToMessageId If the message is a reply, ID of the original message
     * @param replyMarkup Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard, instructions to remove reply keyboard or to force a reply from the user.
     *
     * more: https://core.telegram.org/bots/api#sendvenue
     */
    suspend fun sendVenue(
        chatId: Recipient,
        latitude: Float,
        longitude: Float,
        title: String,
        address: String,
        foursquareId: String? = null,
        foursquareType: String? = null,
        disableNotification: Boolean? = null,
        replyToMessageId: Int? = null,
        replyMarkup: ReplyMarkup? = null
    ): Message

    /**
     * Use this method to send phone contacts. On success, the sent Message is returned.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     * @param phoneNumber Contact's phone number
     * @param firstName Contact's first name
     * @param lastName Contact's last name
     * @param vcard Additional data about the contact in the form of a vCard, 0-2048 bytes
     * @param disableNotification Sends the message silently. Users will receive a notification with no sound.
     * @param replyToMessageId If the message is a reply, ID of the original message
     * @param replyMarkup Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard, instructions to remove keyboard or to force a reply from the user.
     *
     * more: https://core.telegram.org/bots/api#sendcontact
     */
    suspend fun sendContact(
        chatId: Recipient,
        phoneNumber: String,
        firstName: String,
        lastName: String? = null,
        vcard: String? = null,
        disableNotification: Boolean? = null,
        replyToMessageId: Int? = null,
        replyMarkup: ReplyMarkup? = null
    ): Message

    /**
     * Use this method when you need to tell the user that something is happening on the bot's side. The status is set for 5 seconds or less (when a message arrives from your bot, Telegram clients clear its typing status). Returns True on success. Example: The ImageBot needs some time to process a request and upload the image. Instead of sending a text message along the lines of “Retrieving image, please wait…”, the bot may use sendChatAction with action = upload_photo. The user will see a “sending photo” status for the bot. We only recommend using this method when a response from the bot will take a noticeable amount of time to arrive.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     * @param action Type of action to broadcast. Choose one, depending on what the user is about to receive: typing for text messages, upload_photo for photos, record_video or upload_video for videos, record_audio or upload_audio for audio files, upload_document for general files, find_location for location data, record_video_note or upload_video_note for video notes.
     *
     * more: https://core.telegram.org/bots/api#sendchataction
     */
    suspend fun sendChatAction(
        chatId: Recipient,
        action: String
    ): Unit

    /**
     * Use this method to get a list of profile pictures for a user. Returns a UserProfilePhotos object.
     *
     * @param userId Unique identifier of the target user
     * @param offset Sequential number of the first photo to be returned. By default, all photos are returned.
     * @param limit Limits the number of photos to be retrieved. Values between 1—100 are accepted. Defaults to 100.
     *
     * more: https://core.telegram.org/bots/api#getuserprofilephotos
     */
    suspend fun getUserProfilePhotos(
        userId: Int,
        offset: Int? = null,
        limit: Int? = null
    ): UserProfilePhotos

    /**
     * Use this method to get basic info about a file and prepare it for downloading. For the moment, bots can download files of up to 20MB in size. On success, a File object is returned. The file can then be downloaded via the link https://api.telegram.org/file/bot<token>/<file_path>, where <file_path> is taken from the response. It is guaranteed that the link will be valid for at least 1 hour. When the link expires, a new one can be requested by calling getFile again.
     *
     * @param fileId File identifier to get info about Note: This function may not preserve the original file name and MIME type. You should save the file's MIME type and name (if available) when the File object is received.
     *
     * more: https://core.telegram.org/bots/api#getfile
     */
    suspend fun getFile(
        fileId: String
    ): File

    /**
     * Use this method to kick a user from a group, a supergroup or a channel. In the case of supergroups and channels, the user will not be able to return to the group on their own using invite links, etc., unless unbanned first. The bot must be an administrator in the chat for this to work and must have the appropriate admin rights. Returns True on success. Note: In regular groups (non-supergroups), this method will only work if the ‘All Members Are Admins’ setting is off in the target group. Otherwise members may only be removed by the group's creator or by the member that added them.
     *
     * @param chatId Unique identifier for the target group or username of the target supergroup or channel (in the format @channelusername)
     * @param userId Unique identifier of the target user
     * @param untilDate Date when the user will be unbanned, unix time. If user is banned for more than 366 days or less than 30 seconds from the current time they are considered to be banned forever
     *
     * more: https://core.telegram.org/bots/api#kickchatmember
     */
    suspend fun kickChatMember(
        chatId: Recipient,
        userId: Int,
        untilDate: Int? = null
    ): Unit

    /**
     * Use this method to unban a previously kicked user in a supergroup or channel. The user will not return to the group or channel automatically, but will be able to join via link, etc. The bot must be an administrator for this to work. Returns True on success.
     *
     * @param chatId Unique identifier for the target group or username of the target supergroup or channel (in the format @username)
     * @param userId Unique identifier of the target user
     *
     * more: https://core.telegram.org/bots/api#unbanchatmember
     */
    suspend fun unbanChatMember(
        chatId: Recipient,
        userId: Int
    ): Unit

    /**
     * Use this method to restrict a user in a supergroup. The bot must be an administrator in the supergroup for this to work and must have the appropriate admin rights. Pass True for all boolean parameters to lift restrictions from a user. Returns True on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup (in the format @supergroupusername)
     * @param userId Unique identifier of the target user
     * @param untilDate Date when restrictions will be lifted for the user, unix time. If user is restricted for more than 366 days or less than 30 seconds from the current time, they are considered to be restricted forever
     * @param canSendMessages Pass True, if the user can send text messages, contacts, locations and venues
     * @param canSendMediaMessages Pass True, if the user can send audios, documents, photos, videos, video notes and voice notes, implies can_send_messages
     * @param canSendOtherMessages Pass True, if the user can send animations, games, stickers and use inline bots, implies can_send_media_messages
     * @param canAddWebPagePreviews Pass True, if the user may add web page previews to their messages, implies can_send_media_messages
     *
     * more: https://core.telegram.org/bots/api#restrictchatmember
     */
    suspend fun restrictChatMember(
        chatId: Recipient,
        userId: Int,
        untilDate: Int? = null,
        canSendMessages: Boolean? = null,
        canSendMediaMessages: Boolean? = null,
        canSendOtherMessages: Boolean? = null,
        canAddWebPagePreviews: Boolean? = null
    ): Unit

    /**
     * Use this method to promote or demote a user in a supergroup or a channel. The bot must be an administrator in the chat for this to work and must have the appropriate admin rights. Pass False for all boolean parameters to demote a user. Returns True on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     * @param userId Unique identifier of the target user
     * @param canChangeInfo Pass True, if the administrator can change chat title, photo and other settings
     * @param canPostMessages Pass True, if the administrator can create channel posts, channels only
     * @param canEditMessages Pass True, if the administrator can edit messages of other users and can pin messages, channels only
     * @param canDeleteMessages Pass True, if the administrator can delete messages of other users
     * @param canInviteUsers Pass True, if the administrator can invite new users to the chat
     * @param canRestrictMembers Pass True, if the administrator can restrict, ban or unban chat members
     * @param canPinMessages Pass True, if the administrator can pin messages, supergroups only
     * @param canPromoteMembers Pass True, if the administrator can add new administrators with a subset of his own privileges or demote administrators that he has promoted, directly or indirectly (promoted by administrators that were appointed by him)
     *
     * more: https://core.telegram.org/bots/api#promotechatmember
     */
    suspend fun promoteChatMember(
        chatId: Recipient,
        userId: Int,
        canChangeInfo: Boolean? = null,
        canPostMessages: Boolean? = null,
        canEditMessages: Boolean? = null,
        canDeleteMessages: Boolean? = null,
        canInviteUsers: Boolean? = null,
        canRestrictMembers: Boolean? = null,
        canPinMessages: Boolean? = null,
        canPromoteMembers: Boolean? = null
    ): Unit

    /**
     * Use this method to generate a new invite link for a chat; any previously generated link is revoked. The bot must be an administrator in the chat for this to work and must have the appropriate admin rights. Returns the new invite link as String on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername) Note: Each administrator in a chat generates their own invite links. Bots can't use invite links generated by other administrators. If you want your bot to work with invite links, it will need to generate its own link using exportChatInviteLink – after this the link will become available to the bot via the getChat method. If your bot needs to generate a new invite link replacing its previous one, use exportChatInviteLink again.
     *
     * more: https://core.telegram.org/bots/api#exportchatinvitelink
     */
    suspend fun exportChatInviteLink(
        chatId: Recipient
    ): String

    /**
     * Use this method to set a new profile photo for the chat. Photos can't be changed for private chats. The bot must be an administrator in the chat for this to work and must have the appropriate admin rights. Returns True on success. Note: In regular groups (non-supergroups), this method will only work if the ‘All Members Are Admins’ setting is off in the target group.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     * @param photo New chat photo, uploaded using multipart/form-data
     *
     * more: https://core.telegram.org/bots/api#setchatphoto
     */
    suspend fun setChatPhoto(
        chatId: Recipient,
        photo: InputFile
    ): Unit

    /**
     * Use this method to delete a chat photo. Photos can't be changed for private chats. The bot must be an administrator in the chat for this to work and must have the appropriate admin rights. Returns True on success. Note: In regular groups (non-supergroups), this method will only work if the ‘All Members Are Admins’ setting is off in the target group.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     *
     * more: https://core.telegram.org/bots/api#deletechatphoto
     */
    suspend fun deleteChatPhoto(
        chatId: Recipient
    ): Unit

    /**
     * Use this method to change the title of a chat. Titles can't be changed for private chats. The bot must be an administrator in the chat for this to work and must have the appropriate admin rights. Returns True on success. Note: In regular groups (non-supergroups), this method will only work if the ‘All Members Are Admins’ setting is off in the target group.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     * @param title New chat title, 1-255 characters
     *
     * more: https://core.telegram.org/bots/api#setchattitle
     */
    suspend fun setChatTitle(
        chatId: Recipient,
        title: String
    ): Unit

    /**
     * Use this method to change the description of a supergroup or a channel. The bot must be an administrator in the chat for this to work and must have the appropriate admin rights. Returns True on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     * @param description New chat description, 0-255 characters
     *
     * more: https://core.telegram.org/bots/api#setchatdescription
     */
    suspend fun setChatDescription(
        chatId: Recipient,
        description: String? = null
    ): Unit

    /**
     * Use this method to pin a message in a group, a supergroup, or a channel.
     *
     * The bot must be an administrator in the chat for this to work and must have the ‘can_pin_messages’ admin right in the supergroup
     * or ‘can_edit_messages’ admin right in the channel.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     * @param messageId Identifier of a message to pin
     * @param disableNotification Pass True, if it is not necessary to send a notification to all chat members about the new pinned message. Notifications are always disabled in channels.
     *
     * more: https://core.telegram.org/bots/api#pinchatmessage
     */
    suspend fun pinChatMessage(
        chatId: Recipient,
        messageId: Int,
        disableNotification: Boolean? = null
    ): Unit

    /**
     * Use this method to unpin a message in a group, a supergroup, or a channel.
     *
     * The bot must be an administrator in the chat for this to work and must have the ‘can_pin_messages’ admin right in the supergroup or
     * ‘can_edit_messages’ admin right in the channel.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     *
     * more: https://core.telegram.org/bots/api#unpinchatmessage
     */
    suspend fun unpinChatMessage(
        chatId: Recipient
    ): Unit

    /**
     * Use this method for your bot to leave a group, supergroup or channel. Returns True on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup or channel (in the format @channelusername)
     *
     * more: https://core.telegram.org/bots/api#leavechat
     */
    suspend fun leaveChat(
        chatId: Recipient
    ): Unit

    /**
     * Use this method to get up to date information about the chat (current name of the user for one-on-one conversations, current username of a user, group or channel, etc.). Returns a Chat object on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup or channel (in the format @channelusername)
     *
     * more: https://core.telegram.org/bots/api#getchat
     */
    suspend fun getChat(
        chatId: Recipient
    ): Chat

    /**
     * Use this method to get a list of administrators in a chat. On success, returns an Array of ChatMember objects that contains information about all chat administrators except other bots. If the chat is a group or a supergroup and no administrators were appointed, only the creator will be returned.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup or channel (in the format @channelusername)
     *
     * more: https://core.telegram.org/bots/api#getchatadministrators
     */
    suspend fun getChatAdministrators(
        chatId: Recipient
    ): List<ChatMember>

    /**
     * Use this method to get the number of members in a chat. Returns Int on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup or channel (in the format @channelusername)
     *
     * more: https://core.telegram.org/bots/api#getchatmemberscount
     */
    suspend fun getChatMembersCount(
        chatId: Recipient
    ): Int

    /**
     * Use this method to get information about a member of a chat. Returns a ChatMember object on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup or channel (in the format @channelusername)
     * @param userId Unique identifier of the target user
     *
     * more: https://core.telegram.org/bots/api#getchatmember
     */
    suspend fun getChatMember(
        chatId: Recipient,
        userId: Int
    ): ChatMember

    /**
     * Use this method to set a new group sticker set for a supergroup. The bot must be an administrator in the chat for this to work and must have the appropriate admin rights. Use the field can_set_sticker_set optionally returned in getChat requests to check if the bot can use this method. Returns True on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup (in the format @supergroupusername)
     * @param stickerSetName Name of the sticker set to be set as the group sticker set
     *
     * more: https://core.telegram.org/bots/api#setchatstickerset
     */
    suspend fun setChatStickerSet(
        chatId: Recipient,
        stickerSetName: String
    ): Unit

    /**
     * Use this method to delete a group sticker set from a supergroup. The bot must be an administrator in the chat for this to work and must have the appropriate admin rights. Use the field can_set_sticker_set optionally returned in getChat requests to check if the bot can use this method. Returns True on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup (in the format @supergroupusername)
     *
     * more: https://core.telegram.org/bots/api#deletechatstickerset
     */
    suspend fun deleteChatStickerSet(
        chatId: Recipient
    ): Unit

    /**
     * Use this method to send answers to callback queries sent from inline keyboards. The answer will be displayed to the user as a notification at the top of the chat screen or as an alert. On success, True is returned. Alternatively, the user can be redirected to the specified Game URL. For this option to work, you must first create a game for your bot via @Botfather and accept the terms. Otherwise, you may use links like t.me/your_bot?start=XXXX that open your bot with a parameter.
     *
     * @param callbackQueryId Unique identifier for the query to be answered
     * @param text Text of the notification. If not specified, nothing will be shown to the user, 0-200 characters
     * @param showAlert If true, an alert will be shown by the client instead of a notification at the top of the chat screen. Defaults to false.
     * @param url URL that will be opened by the user's client. If you have created a Game and accepted the conditions via @Botfather, specify the URL that opens your game – note that this will only work if the query comes from a callback_game button. Otherwise, you may use links like t.me/your_bot?start=XXXX that open your bot with a parameter.
     *
     * more: https://core.telegram.org/bots/api#answercallbackquery
     */
    suspend fun answerCallbackQuery(
        callbackQueryId: String,
        text: String? = null,
        showAlert: Boolean? = null,
        url: String? = null
    ): Unit
    //</editor-fold>

    //<editor-fold desc="updating messages">
    /**
     * Use this method to edit live location messages sent via the bot (for inline bots).
     * A location can be edited until its live_period expires or
     * editing is explicitly disabled by a call to [StopMessageLiveLocationInline].
     *
     * @param inlineMessageId Identifier of the inline message
     * @param latitude Latitude of new location
     * @param longitude Longitude of new location
     * @param replyMarkup A JSON-serialized object for a new inline keyboard.
     *
     * More: https://core.telegram.org/bots/api#editmessagelivelocation
     */
    suspend fun editMessageLiveLocation(
        inlineMessageId: String,
        latitude: Float,
        longitude: Float,
        replyMarkup: InlineKeyboardMarkup? = null
    ): Unit


    /**
     * Use this method to edit live location messages sent by the bot.
     * A location can be edited until its live_period expires or
     * editing is explicitly disabled by a call to [StopMessageLiveLocation].
     * On success, the edited Message is returned.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     * @param messageId Identifier of the sent message
     * @param latitude Latitude of new location
     * @param longitude Longitude of new location
     * @param replyMarkup A JSON-serialized object for a new inline keyboard.
     *
     * More: https://core.telegram.org/bots/api#editmessagelivelocation
     */
    suspend fun editMessageLiveLocation(
        chatId: Recipient,
        messageId: Int,
        latitude: Float,
        longitude: Float,
        replyMarkup: InlineKeyboardMarkup? = null
    ): Message

    /**
     * Use this method to edit text and game messages sent via the bot (for inline bots).
     *
     * @param inlineMessageId Identifier of the inline message
     * @param text New text of the message
     * @param parseMode Send Markdown or HTML, if you want Telegram apps to show bold, italic, fixed-width text or inline URLs in your bot's message.
     * @param disableWebPagePreview Disables link previews for links in this message
     * @param replyMarkup A JSON-serialized object for an inline keyboard.
     *
     * more: https://core.telegram.org/bots/api#editmessagetext
     */
    suspend fun editMessageText(
        inlineMessageId: String,
        text: String,
        parseMode: ParseMode? = null,
        disableWebPagePreview: Boolean? = null,
        replyMarkup: InlineKeyboardMarkup? = null
    ): Unit


    /**
     * Use this method to edit text and game messages sent by the bot.
     * On success, the edited Message is returned.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     * @param messageId Identifier of the sent message
     * @param text New text of the message
     * @param parseMode Send Markdown or HTML, if you want Telegram apps to show bold, italic, fixed-width text or inline URLs in your bot's message.
     * @param disableWebPagePreview Disables link previews for links in this message
     * @param replyMarkup A JSON-serialized object for an inline keyboard.
     *
     * more: https://core.telegram.org/bots/api#editmessagetext
     */
    suspend fun editMessageText(
        chatId: Recipient,
        messageId: Int,
        text: String,
        parseMode: ParseMode? = null,
        disableWebPagePreview: Boolean? = null,
        replyMarkup: InlineKeyboardMarkup? = null
    ): Message


    /**
     * Use this method to edit captions of messages sent via the bot (for inline bots).
     *
     * @param inlineMessageId Identifier of the inline message
     * @param caption New caption of the message
     * @param parseMode Send Markdown or HTML, if you want Telegram apps to show bold, italic, fixed-width text or inline URLs in the media caption.
     * @param replyMarkup A JSON-serialized object for an inline keyboard.
     *
     * more: https://core.telegram.org/bots/api#editmessagecaption
     */
    suspend fun editMessageCaption(
        inlineMessageId: String,
        caption: String? = null,
        parseMode: ParseMode? = null,
        replyMarkup: InlineKeyboardMarkup? = null
    ): Unit

    /**
     * Use this method to edit captions of messages sent by the bot.
     * On success, the edited Message is returned.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     * @param messageId Identifier of the sent message
     * @param caption New caption of the message
     * @param parseMode Send Markdown or HTML, if you want Telegram apps to show bold, italic, fixed-width text or inline URLs in the media caption.
     * @param replyMarkup A JSON-serialized object for an inline keyboard.
     *
     * more: https://core.telegram.org/bots/api#editmessagecaption
     */
    suspend fun editMessageCaption(
        chatId: Recipient,
        messageId: Int,
        caption: String? = null,
        parseMode: ParseMode? = null,
        replyMarkup: InlineKeyboardMarkup? = null
    ): Message


    /**
     * Use this method to edit animation, audio, document, photo, or video messages.
     * If a message is a part of a message album, then it can be edited only to a photo or a video.
     * Otherwise, message type can be changed arbitrarily.
     * By this method new file can't be uploaded (but can by another [editMessageMedia]).
     * Use previously uploaded file via its file_id or specify a URL.
     *
     * @param inlineMessageId Identifier of the inline message
     * @param media A JSON-serialized object for a new media content of the message
     * @param replyMarkup A JSON-serialized object for a new inline keyboard.
     *
     * more: https://core.telegram.org/bots/api#editmessagemedia
     */
    suspend fun editMessageMedia(
        inlineMessageId: String,
        media: InputMedia,
        replyMarkup: InlineKeyboardMarkup? = null
    ): Unit

    /**
     * Use this method to edit animation, audio, document, photo, or video messages.
     * If a message is a part of a message album, then it can be edited only to a photo or a video.
     * Otherwise, message type can be changed arbitrarily.
     * On success, the edited Message is returned.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     * @param messageId Identifier of the sent message
     * @param media A JSON-serialized object for a new media content of the message
     * @param replyMarkup A JSON-serialized object for a new inline keyboard.
     *
     * more: https://core.telegram.org/bots/api#editmessagemedia
     */
    suspend fun editMessageMedia(
        chatId: Recipient,
        messageId: Int,
        media: InputMedia,
        replyMarkup: InlineKeyboardMarkup? = null
    ): Message


    /**
     * Use this method to edit only the reply markup of messages sent via the bot (for inline bots).
     *
     * @param inlineMessageId Identifier of the inline message
     * @param replyMarkup A JSON-serialized object for an inline keyboard.
     *
     * more: https://core.telegram.org/bots/api#editmessagereplymarkup
     */
    suspend fun editMessageReplyMarkup(
        inlineMessageId: String,
        replyMarkup: InlineKeyboardMarkup? = null
    ): Unit

    /**
     * Use this method to edit only the reply markup of messages sent by the bot.
     * On success, the edited Message is returned.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     * @param messageId Identifier of the sent message
     * @param replyMarkup A JSON-serialized object for an inline keyboard.
     *
     * more: https://core.telegram.org/bots/api#editmessagereplymarkup
     */
    suspend fun editMessageReplyMarkup(
        chatId: Recipient,
        messageId: Int,
        replyMarkup: InlineKeyboardMarkup? = null
    ): Message


    /**
     * Use this method to stop updating a live location message sent via the bot (for inline bots) before live_period expires.
     *
     * @param inlineMessageId Identifier of the inline message
     *
     * more: https://core.telegram.org/bots/api#stopmessagelivelocation
     */
    suspend fun stopMessageLiveLocation(
        inlineMessageId: String
    ): Unit

    /**
     * Use this method to stop updating a live location message sent by the bot before live_period expires.
     * On success, the sent Message is returned.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     * @param messageId Identifier of the sent message
     *
     * more: https://core.telegram.org/bots/api#stopmessagelivelocation
     */
    suspend fun stopMessageLiveLocation(
        chatId: Recipient,
        messageId: Int
    ): Message
    //</editor-fold>

    //<editor-fold desc="payments">
    /**
     * Use this method to send invoices.
     * On success, the sent Message is returned.
     *
     * @param chatId Unique identifier for the target private chat
     * @param title Product name, 1-32 characters
     * @param description Product description, 1-255 characters
     * @param payload Bot-defined invoice payload, 1-128 bytes. This will not be displayed to the user, use for your internal processes.
     * @param providerToken Payments provider token, obtained via Botfather
     * @param startParameter Unique deep-linking parameter that can be used to generate this invoice when used as a start parameter
     * @param currency Three-letter ISO 4217 currency code, see more on currencies
     * @param prices Price breakdown, a list of components (e.g. product price, tax, discount, delivery cost, delivery tax, bonus, etc.)
     * @param providerData JSON-encoded data about the invoice, which will be shared with the payment provider. A detailed description of required fields should be provided by the payment provider.
     * @param photoUrl URL of the product photo for the invoice. Can be a photo of the goods or a marketing image for a service. People like it better when they see what they are paying for.
     * @param photoSize Photo size
     * @param photoWidth Photo width
     * @param photoHeight Photo height
     * @param needName Pass True, if you require the user's full name to complete the order
     * @param needPhoneNumber Pass True, if you require the user's phone number to complete the order
     * @param needEmail Pass True, if you require the user's email address to complete the order
     * @param needShippingAddress Pass True, if you require the user's shipping address to complete the order
     * @param sendPhoneNumberToProvider Pass True, if user's phone number should be sent to provider
     * @param sendEmailToProvider Pass True, if user's email address should be sent to provider
     * @param isFlexible Pass True, if the final price depends on the shipping method
     * @param disableNotification Sends the message silently. Users will receive a notification with no sound.
     * @param replyToMessageId If the message is a reply, ID of the original message
     * @param replyMarkup A JSON-serialized object for an inline keyboard. If empty, one 'Pay total price' button will be shown. If not empty, the first button must be a Pay button.
     *
     * more: https://core.telegram.org/bots/api#sendinvoice
     */
    suspend fun sendInvoice(
        chatId: Recipient,
        title: String,
        description: String,
        payload: String,
        providerToken: String,
        startParameter: String,
        currency: String,
        prices: List<LabeledPrice>,
        providerData: String? = null,
        photoUrl: String? = null,
        photoSize: Int? = null,
        photoWidth: Int? = null,
        photoHeight: Int? = null,
        needName: Boolean? = null,
        needPhoneNumber: Boolean? = null,
        needEmail: Boolean? = null,
        needShippingAddress: Boolean? = null,
        sendPhoneNumberToProvider: Boolean? = null,
        sendEmailToProvider: Boolean? = null,
        isFlexible: Boolean? = null,
        disableNotification: Boolean? = null,
        replyToMessageId: Int? = null,
        replyMarkup: InlineKeyboardMarkup? = null
    ): Message

    /**
     * If you sent an invoice requesting a shipping address and the parameter is_flexible was specified, the Bot API will send an Update with a shipping_query field to the bot. Use this method to reply to shipping queries.
     *
     * @param shippingQueryId Unique identifier for the query to be answered
     * @param ok Specify True if delivery to the specified address is possible and False if there are any problems (for example, if delivery to the specified address is not possible)
     * @param shippingOptions Required if ok is True. A JSON-serialized array of available shipping options.
     * @param errorMessage Required if ok is False. Error message in human readable form that explains why it is impossible to complete the order (e.g. "Sorry, delivery to your desired address is unavailable'). Telegram will display this message to the user.
     *
     * more: https://core.telegram.org/bots/api#answershippingquery
     */
    suspend fun answerShippingQuery(
        shippingQueryId: String,
        ok: Boolean,
        shippingOptions: List<ShippingOption>? = null,
        errorMessage: String? = null
    ): Unit

    /**
     * Once the user has confirmed their payment and shipping details, the Bot API sends the final confirmation in the form of an Update with the field pre_checkout_query. Use this method to respond to such pre-checkout queries.
     * Note: The Bot API must receive an answer within 10 seconds after the pre-checkout query was sent.
     *
     * @param preCheckoutQueryId Unique identifier for the query to be answered
     * @param ok Specify True if everything is alright (goods are available, etc.) and the bot is ready to proceed with the order. Use False if there are any problems.
     * @param errorMessage Required if ok is False. Error message in human readable form that explains the reason for failure to proceed with the checkout (e.g. "Sorry, somebody just bought the last of our amazing black T-shirts while you were busy filling out your payment details. Please choose a different color or garment!"). Telegram will display this message to the user.
     *
     * more: https://core.telegram.org/bots/api#answerprecheckoutquery
     */
    suspend fun answerPreCheckoutQuery(
        preCheckoutQueryId: String,
        ok: Boolean,
        errorMessage: String? = null
    ): Unit
    //</editor-fold>

    //<editor-fold desc="tg-passport">
    /**
     * Informs a user that some of the Telegram Passport elements they provided contains errors. The user will not be able to re-submit their Passport to you until the errors are fixed (the contents of the field for which you returned the error must change). Returns True on success. Use this if the data submitted by the user doesn't satisfy the standards your service requires for any reason. For example, if a birthday date seems invalid, a submitted document is blurry, a scan shows evidence of tampering, etc. Supply some details in the error message to make sure the user knows how to correct the issues.
     *
     * @param userId User identifier
     * @param errors A JSON-serialized array describing the errors
     *
     * more: https://core.telegram.org/bots/api#setpassportdataerrors
     */
    suspend fun setPassportDataErrors(
        userId: Int,
        errors: List<PassportElementError>
    ): Unit
    //</editor-fold>

    //<editor-fold desc="polls">
    /**
     * Use this method to send a native poll. A native poll can't be sent to a private chat.
     * On success, the sent Message is returned.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername). A native poll can't be sent to a private chat.
     * @param question Poll question, 1-255 characters
     * @param options List of answer options, 2-10 strings 1-100 characters each
     * @param disableNotification Sends the message silently. Users will receive a notification with no sound.
     * @param replyToMessageId If the message is a reply, ID of the original message
     * @param replyMarkup Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard, instructions to remove reply keyboard or to force a reply from the user.
     *
     * more: https://core.telegram.org/bots/api#sendpoll
     */
    suspend fun sendPoll(
        chatId: Recipient,
        question: String,
        options: List<String>,
        disableNotification: Boolean? = null,
        replyToMessageId: Int? = null,
        replyMarkup: ReplyMarkup? = null
    ): Message

    /**
     * Use this method to stop a poll which was sent by the bot.
     * On success, the stopped Poll with the final results is returned.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     * @param messageId Identifier of the original message with the poll
     * @param replyMarkup A JSON-serialized object for a new message inline keyboard.
     *
     * more: https://core.telegram.org/bots/api#stoppoll
     */
    suspend fun stopPoll(
        chatId: Recipient,
        messageId: Int,
        replyMarkup: InlineKeyboardMarkup? = null
    ): Poll
    //</editor-fold>

    //</editor-fold>

    fun close(): Unit
}
