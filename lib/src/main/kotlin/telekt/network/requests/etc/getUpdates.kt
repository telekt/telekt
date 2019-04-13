package telekt.network.requests.etc

import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import telekt.network.TelegramMethod
import telekt.network.requests.abstracts.SimpleRequest
import telekt.types.Update

/**
 * [GetUpdates] request
 * Use this method to receive incoming updates using long polling (wiki)[https://en.wikipedia.org/wiki/Push_technology#Long_polling].
 * An Array of [Update] objects is returned.
 *
 * Notes
 *   1. This method will not work if an outgoing webhook is set up.
 *   2. In order to avoid getting duplicate updates, recalculate offset after each server response.
 *
 * More: https://core.telegram.org/bots/api#getupdates
 */
@Serializable data class GetUpdates(
    /**
     * Identifier of the first update to be returned.
     * Must be greater by one than the highest among the identifiers of previously received updates.
     * By default, updates starting with the earliest unconfirmed update are returned.
     * An update is considered confirmed as soon as getUpdates is called with an offset higher than its update_id.
     * The negative offset can be specified to retrieve updates starting from -offset update from the end of the updates queue.
     * All previous updates will forgotten.
     */
    @Optional val offset: Int? = null,
    /** Limits the number of updates to be retrieved. Values between 1—100 are accepted. Defaults to 100. */
    @Optional val limit: Byte? = null,
    /**
     * Timeout in seconds for long polling. Defaults to 0, i.e. usual short polling.
     * Should be positive, short polling should be used for testing purposes only.
     */
    @Optional val timeout: Int? = null,
    /**
     * List the types of updates you want your bot to receive.
     * For example, specify [“message”, “edited_channel_post”, “callback_query”] to only receive updates of these types.
     * See [Update] for a complete list of available update types. Specify an empty list to receive all updates regardless of type (default).
     * If not specified, the previous setting will be used.
     * Please note that this parameter doesn't affect updates created before the call to the getUpdates,
     * so unwanted updates may be received for a short period of time.
     */
    @Optional val allowedUpdates: List<String>? = null
) : SimpleRequest<List<Update>>() {
    @Transient override val method: TelegramMethod = TelegramMethod.getUpdates
    @Transient override val resultDeserializer: KSerializer<out List<Update>> = Update.serializer().list
    override fun stringify(json: Json): String = json.stringify(GetUpdates.serializer(), this)
}
