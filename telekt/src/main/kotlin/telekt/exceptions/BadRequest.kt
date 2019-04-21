package rocks.waffle.telekt.exceptions

import kotlin.reflect.KFunction

object BadRequestExceptionDetector : Detector<BadRequest>() {
    override val exceptions: MutableList<Pair<KFunction<BadRequest>, String>> by lazy {
        getExceptions<ErrorTextBadRequest, BadRequest>(ReflectionsObj.reflections) { match }
    }
}

open class BadRequest(message: String) : TelegramAPIException(message)

//<editor-fold desc="MessageExceptions">
open class MessageException(message: String) : BadRequest(message)

@ErrorTextBadRequest("message is not modified")
/** Will be raised when you try to set new text is equals to current text. */
class MessageNotModified(message: String) : MessageException(message)

@ErrorTextBadRequest("message to forward not found")
/** Will be raised when you try to forward very old or deleted or unknown message. */
class MessageToForwardNotFound(message: String) : MessageException(message)

@ErrorTextBadRequest("message to delete not found")
/** Will be raised when you try to delete very old or deleted or unknown message. */
class MessageToDeleteNotFound(message: String) : MessageException(message)

@ErrorTextBadRequest("message identifier is not specified")
class MessageIdentifierNotSpecified(message: String) : MessageException(message)

@ErrorTextBadRequest("Message text is empty")
class MessageTextIsEmpty(message: String) : MessageException(message)

@ErrorTextBadRequest("message can't be edited")
class MessageCantBeEdited(message: String) : MessageException(message)

@ErrorTextBadRequest("message can't be deleted")
class MessageCantBeDeleted(message: String) : MessageException(message)

@ErrorTextBadRequest("message to edit not found")
class MessageToEditNotFound(message: String) : MessageException(message)

@ErrorTextBadRequest("message is too long")
class MessageIsTooLong(message: String) : MessageException(message)

/** Will be raised when you try to send media group with more than 10 items. */
@ErrorTextBadRequest("Too much messages to send as an album")
class ToMuchMessages(message: String) : MessageException(message)
//</editor-fold>

//<editor-fold desc="PollExceptions">
open class PollException(message: String) : BadRequest(message)

@ErrorTextBadRequest("message with poll to stop not found")
/** Will be raised when you try to stop poll with message without poll */
class MessageWithPollNotFound(message: String) : PollException(message)

@ErrorTextBadRequest("message is not a poll")
/** Will be raised when you try to stop poll with message without poll */
class MessageIsNotAPoll(message: String) : PollException(message)

@ErrorTextBadRequest("polls can't be sent to private chats")
class PollsCantBeSentToPrivateChats(message: String) : PollException(message)


//<editor-fold desc="PollSizeExceptions">
open class PollSizeException(message: String) : BadRequest(message)

@ErrorTextBadRequest("poll must have at least 2 option")
class PollMustHaveMoreOptions(message: String) : PollSizeException(message)

@ErrorTextBadRequest("poll can't have more than 10 options")
class PollCantHaveMoreOptions(message: String) : PollSizeException(message)

@ErrorTextBadRequest("poll options must be non-empty")
class PollOptionsMustBeNonEmpty(message: String) : PollSizeException(message)

@ErrorTextBadRequest("poll question must be non-empty")
class PollQuestionMustBeNonEmpty(message: String) : PollSizeException(message)

@ErrorTextBadRequest("poll options length must not exceed 100")
class PollOptionsLengthTooLong(message: String) : PollSizeException(message)

@ErrorTextBadRequest("poll question length must not exceed 255")
class PollQuestionLengthTooLong(message: String) : PollSizeException(message)
//</editor-fold>


@ErrorTextBadRequest("poll can't be stopped")
class PollCantBeStopped(message: String) : PollException(message)

@ErrorTextBadRequest("poll has already been closed")
class PollHasAlreadyBeenClosed(message: String) : PollException(message)
//</editor-fold>

@ErrorTextBadRequest("object expected as reply markup")
class ObjectExpectedAsReplyMarkup(message: String) : BadRequest(message)

@ErrorTextBadRequest("inline keyboard expected")
class InlineKeyboardExpected(message: String) : BadRequest(message)

@ErrorTextBadRequest("chat not found")
class ChatNotFound(message: String) : BadRequest(message)

@ErrorTextBadRequest("user_id_invalid")
class InvalidUserId(message: String) : BadRequest(message)

@ErrorTextBadRequest("chat description is not modified")
class ChatDescriptionIsNotModified(message: String) : BadRequest(message)

@ErrorTextBadRequest("QUERY_ID_INVALID")
class InvalidQueryID(message: String) : BadRequest(message)

@ErrorTextBadRequest("PEER_ID_INVALID")
class InvalidPeerID(message: String) : BadRequest(message)

@ErrorTextBadRequest("Failed to get HTTP URL content")
class InvalidHTTPUrlContent(message: String) : BadRequest(message)

@ErrorTextBadRequest("BUTTON_URL_INVALID")
class ButtonURLInvalid(message: String) : BadRequest(message)

@ErrorTextBadRequest("URL host is empty")
class URLHostIsEmpty(message: String) : BadRequest(message)

@ErrorTextBadRequest("START_PARAM_INVALID")
class StartParamInvalid(message: String) : BadRequest(message)

@ErrorTextBadRequest("BUTTON_DATA_INVALID")
class ButtonDataInvalid(message: String) : BadRequest(message)

@ErrorTextBadRequest("wrong file identifier/HTTP URL specified")
class WrongFileIdentifier(message: String) : BadRequest(message)

@ErrorTextBadRequest("group is deactivated")
class GroupDeactivated(message: String) : BadRequest(message)

@ErrorTextBadRequest("Photo should be uploaded as an InputFile")
class PhotoAsInputFileRequired(message: String) : BadRequest(message)

@ErrorTextBadRequest("STICKERSET_INVALID")
class InvalidStickersSet(message: String) : BadRequest(message)

@ErrorTextBadRequest("there is no sticker in the request")
class NoStickerInRequest(message: String) : BadRequest(message)

@ErrorTextBadRequest("CHAT_ADMIN_REQUIRED")
class ChatAdminRequired(message: String) : BadRequest(message)

@ErrorTextBadRequest("need administrator rights in the channel chat")
class NeedAdministratorRightsInTheChannel(message: String) : BadRequest(message)

@ErrorTextBadRequest("not enough rights to pin a message")
class NotEnoughRightsToPinMessage(message: String) : BadRequest(message)

@ErrorTextBadRequest("method is available only for supergroups and channel")
class MethodNotAvailableInPrivateChats(message: String) : BadRequest(message)

@ErrorTextBadRequest("can't demote chat creator")
class CantDemoteChatCreator(message: String) : BadRequest(message)

@ErrorTextBadRequest("can't restrict self")
class CantRestrictSelf(message: String) : BadRequest(message)

@ErrorTextBadRequest("PHOTO_INVALID_DIMENSIONS")
class PhotoDimensions(message: String) : BadRequest(message)

@ErrorTextBadRequest("supergroup members are unavailable")
class UnavailableMembers(message: String) : BadRequest(message)

@ErrorTextBadRequest("type of file mismatch")
class TypeOfFileMismatch(message: String) : BadRequest(message)

@ErrorTextBadRequest("wrong remote file id specified")
class WrongRemoteFileIdSpecified(message: String) : BadRequest(message)

@ErrorTextBadRequest("PAYMENT_PROVIDER_INVALID")
class PaymentProviderInvalid(message: String) : BadRequest(message)

@ErrorTextBadRequest("currency_total_amount_invalid")
class CurrencyTotalAmountInvalid(message: String) : BadRequest(message)

//<editor-fold desc="BadWebhookExceptions">
open class BadWebhook(message: String) : BadRequest(message)

@ErrorTextBadRequest("HTTPS url must be provided for webhook")
class WebhookRequireHTTPS(message: String) : BadWebhook(message)

@ErrorTextBadRequest("Webhook can be set up only on ports 80, 88, 443 or 8443")
class BadWebhookPort(message: String) : BadWebhook(message)

@ErrorTextBadRequest("getaddrinfo: Temporary failure in name resolution")
class BadWebhookAddrInfo(message: String) : BadWebhook(message)

@ErrorTextBadRequest("failed to resolve host: no address associated with hostname")
class BadWebhookNoAddressAssociatedWithHostname(message: String) : BadWebhook(message)
//</editor-fold>

@ErrorTextBadRequest("can't parse entities")
class CantParseEntities(message: String) : BadRequest(message)

@ErrorTextBadRequest("result_id_duplicate")
class ResultIdDuplicate(message: String) : BadRequest(message)

@ErrorTextBadRequest("unsupported URL protocol")
class UnsupportedURLProtocol(message: String) : BadRequest(message)
