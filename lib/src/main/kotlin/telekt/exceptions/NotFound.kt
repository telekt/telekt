package telekt.exceptions

import kotlin.reflect.KFunction

open class NotFound(message: String) : TelegramAPIException(message)

@ErrorTextNotFound("method not found")
class MethodNotKnown(message: String) : NotFound(message)

object NotFoundExceptionDetector : Detector<NotFound>() {
    override val exceptions: MutableList<Pair<KFunction<NotFound>, String>> by lazy {
        getExceptions<ErrorTextNotFound, NotFound>(ReflectionsObj.reflections) { match }
    }
}
