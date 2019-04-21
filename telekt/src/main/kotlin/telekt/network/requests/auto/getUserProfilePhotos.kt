package rocks.waffle.telekt.network.requests.auto

import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import rocks.waffle.telekt.network.TelegramMethod
import rocks.waffle.telekt.network.requests.abstracts.SimpleRequest
import rocks.waffle.telekt.types.UserProfilePhotos


/**
 * [GetUserProfilePhotos] request.
 * Use this method to get a list of profile pictures for a user. Returns a UserProfilePhotos object.
 * More: https://core.telegram.org/bots/api#getuserprofilephotos
 */
@Serializable data class GetUserProfilePhotos(
    /** Unique identifier of the target user */
    @SerialName("user_id") val userId: Int,
    /** Sequential number of the first photo to be returned. By default, all photos are returned. */
    @Optional val offset: Int? = null,
    /** Limits the number of photos to be retrieved. Values between 1—100 are accepted. Defaults to 100. */
    @Optional val limit: Int? = null
) : SimpleRequest<UserProfilePhotos>() {
    @Transient override val method = TelegramMethod.getUserProfilePhotos
    @Transient override val resultDeserializer: KSerializer<out UserProfilePhotos> = UserProfilePhotos.serializer()
    override fun stringify(json: Json): String = json.stringify(GetUserProfilePhotos.serializer(), this)
}
