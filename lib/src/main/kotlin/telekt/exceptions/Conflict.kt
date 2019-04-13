package telekt.exceptions

import kotlin.reflect.KFunction

open class ConflictException(message: String) : TelegramAPIException(message)

@ErrorTextConflict("terminated by other getUpdates request")
class TerminatedByOtherGetUpdates(message: String) : ConflictException(message)

@ErrorTextConflict("can't use getUpdates method while webhook is active")
class CantGetUpdates(message: String) : ConflictException(message)

object ConflictExceptionDetector : Detector<ConflictException>() {
    override val exceptions: MutableList<Pair<KFunction<ConflictException>, String>> by lazy {
        getExceptions<ErrorTextConflict, ConflictException>(ReflectionsObj.reflections) { match }
    }
}
