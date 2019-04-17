package rocks.waffle.telekt.exceptions

import kotlin.reflect.KFunction


object UnauthorizedDetector : Detector<Unauthorized>() {
    override val exceptions: MutableList<Pair<KFunction<Unauthorized>, String>> by lazy {
        getExceptions<ErrorTextUnauthorized, Unauthorized>(ReflectionsObj.reflections) { match }
    }
}

open class Unauthorized(message: String) : TelegramAPIException(message)

@ErrorTextUnauthorized("Bot was kicked from a chat")
class BotKicked(message: String) : Unauthorized(message)

@ErrorTextUnauthorized("bot was blocked by the user")
class BotBlocked(message: String) : Unauthorized(message)

@ErrorTextUnauthorized("user is deactivated")
class UserDeactivated(message: String) : Unauthorized(message)

@ErrorTextUnauthorized("bot can't initiate conversation with a user")
class CantInitiateConversation(message: String) : Unauthorized(message)

@ErrorTextUnauthorized("bot can't send messages to bots")
class CantTalkWithBots(message: String) : Unauthorized(message)
