package rocks.waffle.telekt.bot

import kotlinx.coroutines.*
import rocks.waffle.telekt.network.Api
import rocks.waffle.telekt.network.InputFile
import rocks.waffle.telekt.network.ktor.KtorApi
import rocks.waffle.telekt.network.requests.auto.*
import rocks.waffle.telekt.network.requests.edit.*
import rocks.waffle.telekt.network.requests.etc.DeleteMessage
import rocks.waffle.telekt.network.requests.etc.GetMe
import rocks.waffle.telekt.network.requests.etc.GetUpdates
import rocks.waffle.telekt.types.*
import rocks.waffle.telekt.types.enums.AllowedUpdate
import rocks.waffle.telekt.types.enums.ParseMode
import rocks.waffle.telekt.types.passport.PassportElementError
import rocks.waffle.telekt.util.Recipient
import java.nio.file.Path
import kotlin.coroutines.CoroutineContext


class BotImpl(
    private val token: String,
    private val api: Api = KtorApi(),
    private val defaultParseMode: ParseMode? = null
) : Bot, CoroutineScope {
    private val job = Job()
    override val coroutineContext: CoroutineContext get() = Dispatchers.Default + job
    override val me: Deferred<User> = async(start = CoroutineStart.LAZY) { getMe() }

    override suspend fun downloadFile(path: String, destination: Path) = api.downloadFile(token, path, destination)

    //<editor-fold desc="api">
    override suspend fun getMe(): User = api.makeRequest(token, GetMe())

    override suspend fun getUpdates(offset: Int?, limit: Byte?, timeout: Int?, allowedUpdates: List<AllowedUpdate>?): List<Update> =
        api.makeRequest(
            token,
            GetUpdates(offset, limit, timeout, allowedUpdates)
        )

    override suspend fun deleteMessage(chatId: Recipient, messageId: Int) =
        api.makeRequest(
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
        api.makeRequest(
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
        api.makeRequest(
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
        api.makeRequest(
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
        api.makeRequest(
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
        api.makeRequest(
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
        api.makeRequest(
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
        api.makeRequest(
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
        api.makeRequest(
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
        api.makeRequest(
            token,
            SendVideoNote(chatId, videoNote, duration, length, thumb, disableNotification, replyToMessageId, replyMarkup)
        )

    override suspend fun sendMediaGroup(
        chatId: Recipient,
        media: List<InputMedia>,
        disableNotification: Boolean?,
        replyToMessageId: Int?
    ): List<Message> =
        api.makeRequest(
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
        api.makeRequest(
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
        api.makeRequest(
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
        api.makeRequest(
            token,
            SendContact(chatId, phoneNumber, firstName, lastName, vcard, disableNotification, replyToMessageId, replyMarkup)
        )

    override suspend fun sendChatAction(chatId: Recipient, action: String): Unit =
        api.makeRequest(
            token,
            SendChatAction(chatId, action)
        )

    override suspend fun getUserProfilePhotos(userId: Int, offset: Int?, limit: Int?): UserProfilePhotos =
        api.makeRequest(
            token,
            GetUserProfilePhotos(userId, offset, limit)
        )

    override suspend fun getFile(fileId: String): File =
        api.makeRequest(
            token,
            GetFile(fileId)
        )

    override suspend fun kickChatMember(chatId: Recipient, userId: Int, untilDate: Int?): Unit =
        api.makeRequest(
            token,
            KickChatMember(chatId, userId, untilDate)
        )

    override suspend fun unbanChatMember(chatId: Recipient, userId: Int): Unit =
        api.makeRequest(
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
        api.makeRequest(
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
        api.makeRequest(
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
        api.makeRequest(
            token,
            ExportChatInviteLink(chatId)
        )

    override suspend fun setChatPhoto(chatId: Recipient, photo: InputFile): Unit =
        api.makeRequest(
            token,
            SetChatPhoto(chatId, photo)
        )

    override suspend fun deleteChatPhoto(chatId: Recipient): Unit =
        api.makeRequest(
            token,
            DeleteChatPhoto(chatId)
        )

    override suspend fun setChatTitle(chatId: Recipient, title: String): Unit =
        api.makeRequest(
            token,
            SetChatTitle(chatId, title)
        )

    override suspend fun setChatDescription(chatId: Recipient, description: String?): Unit =
        api.makeRequest(
            token,
            SetChatDescription(chatId, description)
        )

    override suspend fun pinChatMessage(chatId: Recipient, messageId: Int, disableNotification: Boolean?): Unit =
        api.makeRequest(
            token,
            PinChatMessage(chatId, messageId, disableNotification)
        )

    override suspend fun unpinChatMessage(chatId: Recipient): Unit =
        api.makeRequest(
            token,
            UnpinChatMessage(chatId)
        )

    override suspend fun leaveChat(chatId: Recipient): Unit =
        api.makeRequest(
            token,
            LeaveChat(chatId)
        )

    override suspend fun getChat(chatId: Recipient): Chat =
        api.makeRequest(
            token,
            GetChat(chatId)
        )

    override suspend fun getChatAdministrators(chatId: Recipient): List<ChatMember> =
        api.makeRequest(
            token,
            GetChatAdministrators(chatId)
        )

    override suspend fun getChatMembersCount(chatId: Recipient): Int =
        api.makeRequest(
            token,
            GetChatMembersCount(chatId)
        )

    override suspend fun getChatMember(chatId: Recipient, userId: Int): ChatMember =
        api.makeRequest(
            token,
            GetChatMember(chatId, userId)
        )

    override suspend fun setChatStickerSet(chatId: Recipient, stickerSetName: String): Unit =
        api.makeRequest(
            token,
            SetChatStickerSet(chatId, stickerSetName)
        )

    override suspend fun deleteChatStickerSet(chatId: Recipient): Unit =
        api.makeRequest(
            token,
            DeleteChatStickerSet(chatId)
        )

    override suspend fun answerCallbackQuery(callbackQueryId: String, text: String?, showAlert: Boolean?, url: String?): Unit =
        api.makeRequest(
            token,
            AnswerCallbackQuery(callbackQueryId, text, showAlert, url)
        )
    //</editor-fold>

    //<editor-fold desc="updating messages">
    override suspend fun editMessageLiveLocation(
        inlineMessageId: String,
        latitude: Float,
        longitude: Float,
        replyMarkup: InlineKeyboardMarkup?
    ): Unit =
        api.makeRequest(
            token,
            EditMessageLiveLocationInline(inlineMessageId, latitude, longitude, replyMarkup)
        )

    override suspend fun editMessageLiveLocation(
        chatId: Recipient,
        messageId: Int,
        latitude: Float,
        longitude: Float,
        replyMarkup: InlineKeyboardMarkup?
    ): Message =
        api.makeRequest(
            token,
            EditMessageLiveLocation(chatId, messageId, latitude, longitude, replyMarkup)
        )

    override suspend fun editMessageText(
        inlineMessageId: String,
        text: String,
        parseMode: ParseMode?,
        disableWebPagePreview: Boolean?,
        replyMarkup: InlineKeyboardMarkup?
    ): Unit =
        api.makeRequest(
            token,
            EditMessageTextInline(inlineMessageId, text, parseMode, disableWebPagePreview, replyMarkup)
        )


    override suspend fun editMessageText(
        chatId: Recipient,
        messageId: Int,
        text: String,
        parseMode: ParseMode?,
        disableWebPagePreview: Boolean?,
        replyMarkup: InlineKeyboardMarkup?
    ): Message =
        api.makeRequest(
            token,
            EditMessageText(chatId, messageId, text, parseMode, disableWebPagePreview, replyMarkup)
        )


    override suspend fun editMessageCaption(
        inlineMessageId: String,
        caption: String?,
        parseMode: ParseMode?,
        replyMarkup: InlineKeyboardMarkup?
    ): Unit =
        api.makeRequest(
            token,
            EditMessageCaptionInline(inlineMessageId, caption, parseMode, replyMarkup)
        )

    override suspend fun editMessageCaption(
        chatId: Recipient,
        messageId: Int,
        caption: String?,
        parseMode: ParseMode?,
        replyMarkup: InlineKeyboardMarkup?
    ): Message =
        api.makeRequest(
            token,
            EditMessageCaption(chatId, messageId, caption, parseMode, replyMarkup)
        )


    override suspend fun editMessageMedia(inlineMessageId: String, media: InputMedia, replyMarkup: InlineKeyboardMarkup?): Unit =
        api.makeRequest(
            token,
            EditMessageMediaInline(inlineMessageId, media, replyMarkup)
        )

    override suspend fun editMessageMedia(
        chatId: Recipient,
        messageId: Int,
        media: InputMedia,
        replyMarkup: InlineKeyboardMarkup?
    ): Message =
        api.makeRequest(
            token,
            EditMessageMedia(chatId, messageId, media, replyMarkup)
        )


    override suspend fun editMessageReplyMarkup(inlineMessageId: String, replyMarkup: InlineKeyboardMarkup?): Unit =
        api.makeRequest(
            token,
            EditMessageReplyMarkupInline(inlineMessageId, replyMarkup)
        )

    override suspend fun editMessageReplyMarkup(chatId: Recipient, messageId: Int, replyMarkup: InlineKeyboardMarkup?): Message =
        api.makeRequest(
            token,
            EditMessageReplyMarkup(chatId, messageId, replyMarkup)
        )


    override suspend fun stopMessageLiveLocation(inlineMessageId: String): Unit =
        api.makeRequest(
            token,
            StopMessageLiveLocationInline(inlineMessageId)
        )

    override suspend fun stopMessageLiveLocation(chatId: Recipient, messageId: Int): Message =
        api.makeRequest(
            token,
            StopMessageLiveLocation(chatId, messageId)
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
        replyMarkup: InlineKeyboardMarkup?
    ): Message =
        api.makeRequest(
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
        api.makeRequest(
            token,
            AnswerShippingQuery(shippingQueryId, ok, shippingOptions, errorMessage)
        )

    override suspend fun answerPreCheckoutQuery(preCheckoutQueryId: String, ok: Boolean, errorMessage: String?): Unit =
        api.makeRequest(
            token,
            AnswerPreCheckoutQuery(preCheckoutQueryId, ok, errorMessage)
        )
    //</editor-fold>

    //<editor-fold desc="tg-passport">
    override suspend fun setPassportDataErrors(userId: Int, errors: List<PassportElementError>): Unit =
        api.makeRequest(
            token,
            SetPassportDataErrors(userId, errors)
        )
    //</editor-fold>

    //<editor-fold desc="polls">
    override suspend fun sendPoll(chatId: Recipient, question: String, options: List<String>, disableNotification: Boolean?, replyToMessageId: Int?, replyMarkup: ReplyMarkup?): Message =
        api.makeRequest(
            token,
            SendPoll(chatId, question, options, disableNotification, replyToMessageId, replyMarkup)
        )

    override suspend fun stopPoll(chatId: Recipient, messageId: Int, replyMarkup: InlineKeyboardMarkup?): Poll =
        api.makeRequest(
            token,
            StopPoll(chatId, messageId, replyMarkup)
        )
    //</editor-fold>

    //</editor-fold>

    override suspend fun close() {
        cancelScope()
        api.close()
    }

    private fun cancelScope() {
        cancel()
    }
}
