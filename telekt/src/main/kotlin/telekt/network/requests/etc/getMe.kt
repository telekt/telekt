package rocks.waffle.telekt.network.requests.etc

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import rocks.waffle.telekt.network.TelegramMethod
import rocks.waffle.telekt.network.requests.abstracts.SimpleRequest
import rocks.waffle.telekt.types.User

/**
 * A simple method for testing your bot's auth token. Requires no parameters.
 * Returns basic information about the bot in form of a [User] object.
 */
class GetMe : SimpleRequest<User>() {
    override val method: TelegramMethod = TelegramMethod.getMe
    override val resultDeserializer: KSerializer<out User> = User.serializer()
    override fun stringify(json: Json): String = ""
}