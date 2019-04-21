package rocks.waffle.telekt.types

import kotlinx.serialization.Optional
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** This object represents a file ready to be downloaded. The file can be downloaded via the link https://api.telegram.org/file/bot<token>/<file_path>. It is guaranteed that the link will be valid for at least 1 hour. When the link expires, a new one can be requested by calling getFile. Maximum file size to download is 20 MB */
@Serializable data class File(
    @SerialName("file_id") val fileId: String,
    @Optional @SerialName("file_size") val fileSize: Int? = null,
    @Optional @SerialName("file_path") val filePath: String? = null
)