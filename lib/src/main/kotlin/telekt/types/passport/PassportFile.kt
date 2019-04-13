package telekt.types.passport

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class PassportFile(
    @SerialName("file_id") val fileId: String,
    @SerialName("file_size") val fileSize: Int,
    @SerialName("file_date") val fileDate: Int
)