package rocks.waffle.telekt.bot

import io.ktor.client.HttpClient
import kotlinx.coroutines.*
import rocks.waffle.telekt.network.Api
import rocks.waffle.telekt.network.DefaultApi
import rocks.waffle.telekt.network.InputFile
import rocks.waffle.telekt.network.TelegramClient
import rocks.waffle.telekt.network.requests.auto.*
import rocks.waffle.telekt.network.requests.edit.*
import rocks.waffle.telekt.network.requests.etc.DeleteMessage
import rocks.waffle.telekt.network.requests.etc.GetMe
import rocks.waffle.telekt.network.requests.etc.GetUpdates
import rocks.waffle.telekt.types.*
import rocks.waffle.telekt.types.enums.AllowedUpdate
import rocks.waffle.telekt.types.enums.ParseMode
import rocks.waffle.telekt.types.passport.PassportElementError
import rocks.waffle.telekt.types.replymarkup.ReplyMarkup
import rocks.waffle.telekt.util.Recipient
import java.nio.file.Path
import kotlin.coroutines.CoroutineContext

const val defaultTimeout: Long = 15000

class KtorBot(
    private val token: String,
    client: HttpClient? = null,
    api: Api = DefaultApi,
    private val defaultParseMode: ParseMode? = null,
    requestTimeout: Long? = null
) : Bot, CoroutineScope {
    private val requestTimeout_ = requestTimeout ?: defaultTimeout
    private val network = TelegramClient(client, api, requestTimeout_)

    //<editor-fold desc="lazy me">
    private val job = Job()
    override val coroutineContext: CoroutineContext get() = Dispatchers.Default + job
    override val me: Deferred<User> = async(start = CoroutineStart.LAZY) { getMe() }
    //</editor-fold>

    override suspend fun downloadFile(path: String, destination: Path) = network.downloadFile(token, path, destination)

    //<editor-fold desc="api methods impl">
    override suspend fun getMe(): User = network.makeRequest(token, GetMe())

    override suspend fun getUpdates(offset: Int?, limit: Byte?, timeout: Int?, allowedUpdates: List<AllowedUpdate>?): List<Update> =
        network.makeRequest(
            token,
            GetUpdates(offset, limit, timeout, allowedUpdates),
            /** ([getUpdates] timeout in millis) + (default request timeout / 2) */
            timeout = timeout?.let { (it * 1000 + requestTimeout_ / 2) }
        )

    override suspend fun setWebhook(url: String, certificate: InputFile?, maxConnections: Int?, allowedUpdates: List<AllowedUpdate>?): Unit =
        network.makeRequest(
            token,
            SetWebhook(url, certificate, maxConnections, allowedUpdates)
        )

    override suspend fun deleteMessage(chatId: Recipient, messageId: Int) =
        network.makeRequest(
            token,
            DeleteMessage(chatId, messageId)
        )

    //<editor-fold desc="auto">
    override suspend fun sendMessage(
        chatId: Recipient,
        text: String,
        parseMode: ParseMode?,
        disableWebPagePreview: Boolean?,
        disableNotification: Boolean?,
        replyToMessageId: Int?,
        replyMarkup: ReplyMarkup?
    ): Message =
        network.makeRequest(
            token,
            SendMessage(
                chatId,
                text,
                parseMode ?: defaultParseMode,
                disableWebPagePreview,
                disableNotification,
                replyToMessageId,
                replyMarkup
            )
        )

    override suspend fun forwardMessage(chatId: Recipient, fromChatId: Recipient, disableNotification: Boolean?, messageId: Int): Message =
        network.makeRequest(
            token,
            ForwardMessage(chatId, fromChatId, disableNotification, messageId)
        )

    override suspend fun sendPhoto(
        chatId: Recipient,
        photo: InputFile,
        caption: String?,
        parseMode: ParseMode?,
        disableNotification: Boolean?,
        replyToMessageId: Int?,
        replyMarkup: ReplyMarkup?
    ): Message =
        network.makeRequest(
            token,
            SendPhoto(chatId, photo, caption, parseMode ?: defaultParseMode, disableNotification, replyToMessageId, replyMarkup)
        )

    override suspend fun sendAudio(
        chatId: Recipient,
        audio: InputFile,
        caption: String?,
        parseMode: ParseMode?,
        duration: Int?,
        performer: String?,
        title: String?,
        thumb: InputFile?,
        disableNotification: Boolean?,
        replyToMessageId: Int?,
        replyMarkup: ReplyMarkup?
    ): Message =
        network.makeRequest(
            token,
            SendAudio(
                chatId,
                audio,
                caption,
                parseMode ?: defaultParseMode,
                duration,
                performer,
                title,
                thumb,
                disableNotification,
                replyToMessageId,
                replyMarkup
            )
        )

    override suspend fun sendDocument(
        chatId: Recipient,
        document: InputFile,
        thumb: InputFile?,
        caption: String?,
        parseMode: ParseMode?,
        disableNotification: Boolean?,
        replyToMessageId: Int?,
        replyMarkup: ReplyMarkup?
    ): Message =
        network.makeRequest(
            token,
            SendDocument(
                chatId,
                document,
                thumb,
                caption,
                parseMode ?: defaultParseMode,
                disableNotification,
                replyToMessageId,
                replyMarkup
            )
        )

    override suspend fun sendVideo(
        chatId: Recipient,
        video: InputFile,
        duration: Int?,
        width: Int?,
        height: Int?,
        thumb: InputFile?,
        caption: String?,
        parseMode: ParseMode?,
        supportsStreaming: Boolean?,
        disableNotification: Boolean?,
        replyToMessageId: Int?,
        replyMarkup: ReplyMarkup?
    ): Message =
        network.makeRequest(
            token,
            SendVideo(
                chatId,
                video,
                duration,
                width,
                height,
                thumb,
                caption,
                parseMode ?: defaultParseMode,
                supportsStreaming,
                disableNotification,
                replyToMessageId,
                replyMarkup
            )
        )

    override suspend fun sendAnimation(
        chatId: Recipient,
        animation: InputFile,
        duration: Int?,
        width: Int?,
        height: Int?,
        thumb: InputFile?,
        caption: String?,
        parseMode: ParseMode?,
        disableNotification: Boolean?,
        replyToMessageId: Int?,
        replyMarkup: ReplyMarkup?
    ): Message =
        network.makeRequest(
            token,
            SendAnimation(
                chatId,
                animation,
                duration,
                width,
                height,
                thumb,
                caption,
                parseMode ?: defaultParseMode,
                disableNotification,
                replyToMessageId,
                replyMarkup
            )
        )

    override suspend fun sendVoice(
        chatId: Recipient,
        voice: InputFile,
        caption: String?,
        parseMode: ParseMode?,
        duration: Int?,
        disableNotification: Boolean?,
        replyToMessageId: Int?,
        replyMarkup: ReplyMarkup?
    ): Message =
        network.makeRequest(
            token,
            SendVoice(chatId, voice, caption, parseMode ?: defaultParseMode, duration, disableNotification, replyToMessageId, replyMarkup)
        )

    override suspend fun sendVideoNote(
        chatId: Recipient,
        videoNote: InputFile,
        duration: Int?,
        length: Int?,
        thumb: InputFile?,
        disableNotification: Boolean?,
        replyToMessageId: Int?,
        replyMarkup: ReplyMarkup?
    ): Message =
        network.makeRequest(
            token,
            SendVideoNote(chatId, videoNote, duration, length, thumb, disableNotification, replyToMessageId, replyMarkup)
        )

    override suspend fun sendMediaGroup(
        chatId: Recipient,
        media: List<InputMedia>,
        disableNotification: Boolean?,
        replyToMessageId: Int?
    ): List<Message> =
        network.makeRequest(
            token,
            SendMediaGroup(chatId, media, disableNotification, replyToMessageId)
        )

    override suspend fun sendLocation(
        chatId: Recipient,
        latitude: Float,
        longitude: Float,
        livePeriod: Int?,
        disableNotification: Boolean?,
        replyToMessageId: Int?,
        replyMarkup: ReplyMarkup?
    ): Message =
        network.makeRequest(
            token,
            SendLocation(chatId, latitude, longitude, livePeriod, disableNotification, replyToMessageId, replyMarkup)
        )

    override suspend fun sendVenue(
        chatId: Recipient,
        latitude: Float,
        longitude: Float,
        title: String,
        address: String,
        foursquareId: String?,
        foursquareType: String?,
        disableNotification: Boolean?,
        replyToMessageId: Int?,
        replyMarkup: ReplyMarkup?
    ): Message =
        network.makeRequest(
            token,
            SendVenue(
                chatId,
                latitude,
                longitude,
                title,
                address,
                foursquareId,
                foursquareType,
                disableNotification,
                replyToMessageId,
                replyMarkup
            )
        )

    override suspend fun sendContact(
        chatId: Recipient,
        phoneNumber: String,
        firstName: String,
        lastName: String?,
        vcard: String?,
        disableNotification: Boolean?,
        replyToMessageId: Int?,
        replyMarkup: ReplyMarkup?
    ): Message =
        network.makeRequest(
            token,
            SendContact(chatId, phoneNumber, firstName, lastName, vcard, disableNotification, replyToMessageId, replyMarkup)
        )

    override suspend fun sendChatAction(chatId: Recipient, action: String): Unit =
        network.makeRequest(
            token,
            SendChatAction(chatId, action)
        )

    override suspend fun getUserProfilePhotos(userId: Int, offset: Int?, limit: Int?): UserProfilePhotos =
        network.makeRequest(
            token,
            GetUserProfilePhotos(userId, offset, limit)
        )

    override suspend fun getFile(fileId: String): File =
        network.makeRequest(
            token,
            GetFile(fileId)
        )

    override suspend fun kickChatMember(chatId: Recipient, userId: Int, untilDate: Int?): Unit =
        network.makeRequest(
            token,
            KickChatMember(chatId, userId, untilDate)
        )

    override suspend fun unbanChatMember(chatId: Recipient, userId: Int): Unit =
        network.makeRequest(
            token,
            UnbanChatMember(chatId, userId)
        )

    override suspend fun restrictChatMember(
        chatId: Recipient,
        userId: Int,
        untilDate: Int?,
        canSendMessages: Boolean?,
        canSendMediaMessages: Boolean?,
        canSendOtherMessages: Boolean?,
        canAddWebPagePreviews: Boolean?
    ): Unit =
        network.makeRequest(
            token,
            RestrictChatMember(
                chatId,
                userId,
                untilDate,
                canSendMessages,
                canSendMediaMessages,
                canSendOtherMessages,
                canAddWebPagePreviews
            )
        )

    override suspend fun promoteChatMember(
        chatId: Recipient,
        userId: Int,
        canChangeInfo: Boolean?,
        canPostMessages: Boolean?,
        canEditMessages: Boolean?,
        canDeleteMessages: Boolean?,
        canInviteUsers: Boolean?,
        canRestrictMembers: Boolean?,
        canPinMessages: Boolean?,
        canPromoteMembers: Boolean?
    ): Unit =
        network.makeRequest(
            token,
            PromoteChatMember(
                chatId,
                userId,
                canChangeInfo,
                canPostMessages,
                canEditMessages,
                canDeleteMessages,
                canInviteUsers,
                canRestrictMembers,
                canPinMessages,
                canPromoteMembers
            )
        )

    override suspend fun exportChatInviteLink(chatId: Recipient): String =
        network.makeRequest(
            token,
            ExportChatInviteLink(chatId)
        )

    override suspend fun setChatPhoto(chatId: Recipient, photo: InputFile): Unit =
        network.makeRequest(
            token,
            SetChatPhoto(chatId, photo)
        )

    override suspend fun deleteChatPhoto(chatId: Recipient): Unit =
        network.makeRequest(
            token,
            DeleteChatPhoto(chatId)
        )

    override suspend fun setChatTitle(chatId: Recipient, title: String): Unit =
        network.makeRequest(
            token,
            SetChatTitle(chatId, title)
        )

    override suspend fun setChatDescription(chatId: Recipient, description: String?): Unit =
        network.makeRequest(
            token,
            SetChatDescription(chatId, description)
        )

    override suspend fun pinChatMessage(chatId: Recipient, messageId: Int, disableNotification: Boolean?): Unit =
        network.makeRequest(
            token,
            PinChatMessage(chatId, messageId, disableNotification)
        )

    override suspend fun unpinChatMessage(chatId: Recipient): Unit =
        network.makeRequest(
            token,
            UnpinChatMessage(chatId)
        )

    override suspend fun leaveChat(chatId: Recipient): Unit =
        network.makeRequest(
            token,
            LeaveChat(chatId)
        )

    override suspend fun getChat(chatId: Recipient): Chat =
        network.makeRequest(
            token,
            GetChat(chatId)
        )

    override suspend fun getChatAdministrators(chatId: Recipient): List<ChatMember> =
        network.makeRequest(
            token,
            GetChatAdministrators(chatId)
        )

    override suspend fun getChatMembersCount(chatId: Recipient): Int =
        network.makeRequest(
            token,
            GetChatMembersCount(chatId)
        )

    override suspend fun getChatMember(chatId: Recipient, userId: Int): ChatMember =
        network.makeRequest(
            token,
            GetChatMember(chatId, userId)
        )

    override suspend fun setChatStickerSet(chatId: Recipient, stickerSetName: String): Unit =
        network.makeRequest(
            token,
            SetChatStickerSet(chatId, stickerSetName)
        )

    override suspend fun deleteChatStickerSet(chatId: Recipient): Unit =
        network.makeRequest(
            token,
            DeleteChatStickerSet(chatId)
        )

    override suspend fun answerCallbackQuery(callbackQueryId: String, text: String?, showAlert: Boolean?, url: String?): Unit =
        network.makeRequest(
            token,
            AnswerCallbackQuery(callbackQueryId, text, showAlert, url)
        )
    //</editor-fold>

    //<editor-fold desc="updating messages">
    override suspend fun editMessageLiveLocation(
        inlineMessageId: String,
        latitude: Float,
        longitude: Float,
        replyMarkup: ReplyMarkup?
    ): Unit =
        network.makeRequest(
            token,
            EditMessageLiveLocationInline(inlineMessageId, latitude, longitude, replyMarkup)
        )

    override suspend fun editMessageLiveLocation(
        chatId: Recipient,
        messageId: Int,
        latitude: Float,
        longitude: Float,
        replyMarkup: ReplyMarkup?
    ): Message =
        network.makeRequest(
            token,
            EditMessageLiveLocation(chatId, messageId, latitude, longitude, replyMarkup)
        )

    override suspend fun editMessageText(
        inlineMessageId: String,
        text: String,
        parseMode: ParseMode?,
        disableWebPagePreview: Boolean?,
        replyMarkup: ReplyMarkup?
    ): Unit =
        network.makeRequest(
            token,
            EditMessageTextInline(inlineMessageId, text, parseMode, disableWebPagePreview, replyMarkup)
        )


    override suspend fun editMessageText(
        chatId: Recipient,
        messageId: Int,
        text: String,
        parseMode: ParseMode?,
        disableWebPagePreview: Boolean?,
        replyMarkup: ReplyMarkup?
    ): Message =
        network.makeRequest(
            token,
            EditMessageText(chatId, messageId, text, parseMode, disableWebPagePreview, replyMarkup)
        )


    override suspend fun editMessageCaption(
        inlineMessageId: String,
        caption: String?,
        parseMode: ParseMode?,
        replyMarkup: ReplyMarkup?
    ): Unit =
        network.makeRequest(
            token,
            EditMessageCaptionInline(inlineMessageId, caption, parseMode, replyMarkup)
        )

    override suspend fun editMessageCaption(
        chatId: Recipient,
        messageId: Int,
        caption: String?,
        parseMode: ParseMode?,
        replyMarkup: ReplyMarkup?
    ): Message =
        network.makeRequest(
            token,
            EditMessageCaption(chatId, messageId, caption, parseMode, replyMarkup)
        )


    override suspend fun editMessageMedia(inlineMessageId: String, media: InputMedia, replyMarkup: ReplyMarkup?): Unit =
        network.makeRequest(
            token,
            EditMessageMediaInline(inlineMessageId, media, replyMarkup)
        )

    override suspend fun editMessageMedia(
        chatId: Recipient,
        messageId: Int,
        media: InputMedia,
        replyMarkup: ReplyMarkup?
    ): Message =
        network.makeRequest(
            token,
            EditMessageMedia(chatId, messageId, media, replyMarkup)
        )


    override suspend fun editMessageReplyMarkup(inlineMessageId: String, replyMarkup: ReplyMarkup?): Unit =
        network.makeRequest(
            token,
            EditMessageReplyMarkupInline(inlineMessageId, replyMarkup)
        )

    override suspend fun editMessageReplyMarkup(chatId: Recipient, messageId: Int, replyMarkup: ReplyMarkup?): Message =
        network.makeRequest(
            token,
            EditMessageReplyMarkup(chatId, messageId, replyMarkup)
        )


    override suspend fun stopMessageLiveLocation(inlineMessageId: String): Unit =
        network.makeRequest(
            token,
            StopMessageLiveLocationInline(inlineMessageId)
        )

    override suspend fun stopMessageLiveLocation(chatId: Recipient, messageId: Int): Message =
        network.makeRequest(
            token,
            StopMessageLiveLocation(chatId, messageId)
        )
    //</editor-fold>

    //<editor-fold desc="inline">
    override suspend fun answerInlineQuery(
        inlineQueryId: String,
        results: List<InlineQueryResult>,
        cacheTime: Int?,
        isPersonal: Boolean?,
        nextOffset: String?,
        switchPmText: String?,
        switchPmParameter: String?
    ): Unit =
        network.makeRequest(
            token,
            AnswerInlineQuery(inlineQueryId, results, cacheTime, isPersonal, nextOffset, switchPmText, switchPmParameter)
        )
    //</editor-fold>

    //<editor-fold desc="payments">
    override suspend fun sendInvoice(
        chatId: Recipient,
        title: String,
        description: String,
        payload: String,
        providerToken: String,
        startParameter: String,
        currency: String,
        prices: List<LabeledPrice>,
        providerData: String?,
        photoUrl: String?,
        photoSize: Int?,
        photoWidth: Int?,
        photoHeight: Int?,
        needName: Boolean?,
        needPhoneNumber: Boolean?,
        needEmail: Boolean?,
        needShippingAddress: Boolean?,
        sendPhoneNumberToProvider: Boolean?,
        sendEmailToProvider: Boolean?,
        isFlexible: Boolean?,
        disableNotification: Boolean?,
        replyToMessageId: Int?,
        replyMarkup: ReplyMarkup?
    ): Message =
        network.makeRequest(
            token,
            SendInvoice(
                chatId,
                title,
                description,
                payload,
                providerToken,
                startParameter,
                currency,
                prices,
                providerData,
                photoUrl,
                photoSize,
                photoWidth,
                photoHeight,
                needName,
                needPhoneNumber,
                needEmail,
                needShippingAddress,
                sendPhoneNumberToProvider,
                sendEmailToProvider,
                isFlexible,
                disableNotification,
                replyToMessageId,
                replyMarkup
            )
        )

    override suspend fun answerShippingQuery(
        shippingQueryId: String,
        ok: Boolean,
        shippingOptions: List<ShippingOption>?,
        errorMessage: String?
    ): Unit =
        network.makeRequest(
            token,
            AnswerShippingQuery(shippingQueryId, ok, shippingOptions, errorMessage)
        )

    override suspend fun answerPreCheckoutQuery(preCheckoutQueryId: String, ok: Boolean, errorMessage: String?): Unit =
        network.makeRequest(
            token,
            AnswerPreCheckoutQuery(preCheckoutQueryId, ok, errorMessage)
        )
    //</editor-fold>

    //<editor-fold desc="tg-passport">
    override suspend fun setPassportDataErrors(userId: Int, errors: List<PassportElementError>): Unit =
        network.makeRequest(
            token,
            SetPassportDataErrors(userId, errors)
        )
    //</editor-fold>

    //<editor-fold desc="polls">
    override suspend fun sendPoll(chatId: Recipient, question: String, options: List<String>, disableNotification: Boolean?, replyToMessageId: Int?, replyMarkup: ReplyMarkup?): Message =
        network.makeRequest(
            token,
            SendPoll(chatId, question, options, disableNotification, replyToMessageId, replyMarkup)
        )

    override suspend fun stopPoll(chatId: Recipient, messageId: Int, replyMarkup: ReplyMarkup?): Poll =
        network.makeRequest(
            token,
            StopPoll(chatId, messageId, replyMarkup)
        )
    //</editor-fold>

    //</editor-fold>

    //<editor-fold desc="close">
    override suspend fun close() {
        network.close()
        job.complete()
        me.cancel()
        job.join()
    }
    //</editor-fold>
}
