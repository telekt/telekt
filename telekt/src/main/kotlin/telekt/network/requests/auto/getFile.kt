package rocks.waffle.telekt.network.requests.auto

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.Json
import rocks.waffle.telekt.network.TelegramMethod
import rocks.waffle.telekt.network.requests.abstracts.SimpleRequest
import rocks.waffle.telekt.types.File


/**
 * [GetFile] request.
 * Use this method to get basic info about a file and prepare it for downloading. For the moment, bots can download files of up to 20MB in size. On success, a File object is returned. The file can then be downloaded via the link https://api.telegram.org/file/bot<token>/<file_path>, where <file_path> is taken from the response. It is guaranteed that the link will be valid for at least 1 hour. When the link expires, a new one can be requested by calling getFile again.
 * More: https://core.telegram.org/bots/api#getfile
 */
@Serializable data class GetFile(
    /** File identifier to get info about Note: This function may not preserve the original file name and MIME type. You should save the file's MIME type and name (if available) when the File object is received. */
    @SerialName("file_id") val fileId: String
) : SimpleRequest<File>() {
    @Transient override val method = TelegramMethod.getFile
    @Transient override val resultDeserializer: KSerializer<out File> = File.serializer()
    override fun stringify(json: Json): String = json.stringify(GetFile.serializer(), this)
}
