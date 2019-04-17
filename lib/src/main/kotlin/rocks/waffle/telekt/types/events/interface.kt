package rocks.waffle.telekt.types.events

import rocks.waffle.telekt.bot.Bot
import rocks.waffle.telekt.fsm.BaseStorage
import rocks.waffle.telekt.fsm.FSMContext
import rocks.waffle.telekt.types.*

interface TelegramEvent

/**
 * Event came from telegram, like [telekt.types.Message], [telekt.types.CallbackQuery] and so on with additional info.
 *
 * @param update the update (from telegram) himself
 * @param bot the bot on which token update came
 * @param storage storage for getting FSMContext
 */
sealed class Event<T : TelegramEvent>(
    val update: T,
    val bot: Bot,
    private val storage: BaseStorage,
    private val getFSMInfo: T.() -> FSMInfo
) {
    /** context of this update */
    val fsmContext: FSMContext by lazy {
        val (chatId, userId) = update.getFSMInfo()
        FSMContext(storage, chatId, userId)
    }

    protected data class FSMInfo(val chatId: Long, val userId: Long)

    operator fun component1(): T = update
    operator fun component2(): FSMContext = fsmContext
    operator fun component3(): Bot = bot
}

class UpdateEvent(update: Update, bot: Bot, storage: BaseStorage) : Event<Update>(update, bot, storage, {
    throw NotImplementedError("UpdateEvent dont implement FSM info")
})

class MessageEvent(Message: Message, bot: Bot, storage: BaseStorage) : Event<Message>(Message, bot, storage, {
    FSMInfo(chat.id, from?.id ?: chat.id) // User in chat OR channel state
})

inline val MessageEvent.message get() = update

class EditedMessageEvent(message: Message, bot: Bot, storage: BaseStorage) : Event<Message>(message, bot, storage, {
    FSMInfo(chat.id, from?.id ?: chat.id) // User in chat OR channel state
})

inline val EditedMessageEvent.editedMessage get() = update

class ChannelPostEvent(message: Message, bot: Bot, storage: BaseStorage) : Event<Message>(message, bot, storage, {
    FSMInfo(chat.id, from?.id ?: chat.id) // User in chat OR channel state
})

inline val ChannelPostEvent.channelPost get() = update

class EditedChannelPostEvent(message: Message, bot: Bot, storage: BaseStorage) : Event<Message>(message, bot, storage, {
    FSMInfo(chat.id, from?.id ?: chat.id) // User in chat OR channel state
})

inline val EditedChannelPostEvent.editedChannelPost get() = update

class InlineQueryEvent(inlineQuery: InlineQuery, bot: Bot, storage: BaseStorage) : Event<InlineQuery>(inlineQuery, bot, storage, {
    FSMInfo(from.id, from.id) // PM
})

inline val InlineQueryEvent.inlineQuery get() = update

class ChosenInlineResultEvent(chosenInlineResult: ChosenInlineResult, bot: Bot, storage: BaseStorage) :
    Event<ChosenInlineResult>(chosenInlineResult, bot, storage, {
        FSMInfo(from.id, from.id) // PM
    })

inline val ChosenInlineResultEvent.chosenInlineResult get() = update

class CallbackQueryEvent(callbackQuery: CallbackQuery, bot: Bot, storage: BaseStorage) : Event<CallbackQuery>(callbackQuery, bot, storage, {
    FSMInfo(from.id, from.id) // PM
})

inline val CallbackQueryEvent.callbackQuery get() = update

class ShippingQueryEvent(shippingQuery: ShippingQuery, bot: Bot, storage: BaseStorage) : Event<ShippingQuery>(shippingQuery, bot, storage, {
    FSMInfo(from.id, from.id) // PM
})

inline val ShippingQueryEvent.shippingQuery get() = update

class PreCheckoutQueryEvent(preCheckoutQuery: PreCheckoutQuery, bot: Bot, storage: BaseStorage) :
    Event<PreCheckoutQuery>(preCheckoutQuery, bot, storage, {
        FSMInfo(from.id, from.id) // PM
    })

inline val PreCheckoutQueryEvent.preCheckoutQuery get() = update
