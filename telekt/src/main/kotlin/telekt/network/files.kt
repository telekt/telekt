@file:Suppress("FunctionName")

package rocks.waffle.telekt.network

import io.ktor.http.Url
import kotlinx.serialization.*
import java.io.File
import java.nio.file.Files
import java.util.*

@Serializable(with = InputFileSerializer::class) sealed class InputFile {
    abstract val fileId: String
}

@Serializer(forClass = InputFile::class) object InputFileSerializer : KSerializer<InputFile> {
    override fun deserialize(decoder: Decoder): InputFile {
        throw NotImplementedError()
    }

    override fun serialize(encoder: Encoder, obj: InputFile) {
        return when (obj) {
            is FileId -> encoder.encodeString(obj.fileId)
            is FileUrl -> encoder.encodeString(obj.url.toString())
            is MultipartFile -> encoder.encodeString("attach://${obj.fileId}")
        }
    }
}

/** [InputFile] factory method. Creates InputFile from file id. */
fun InputFile(fileId: String): InputFile =
    FileId(fileId)

/** [InputFile] factory method. Creates InputFile from file from fs. */
fun InputFile(file: File, mimeType: String? = null, filename: String? = null): InputFile =
    MultipartFile(file, mimeType, filename)

/** [InputFile] factory method. Creates InputFile from URL. */
fun InputFile(url: Url): InputFile =
    FileUrl(url)

/**
 * Contains info about file for sending
 */
class MultipartFile(
    val file: File,
    val mimeType: String,
    val filename: String
) : InputFile() {
    override val fileId: String = "${UUID.randomUUID()}.${file.extension}"

    companion object {
        operator fun invoke(path: String, mimeType: String? = null, filename: String? = null): MultipartFile = File(path).let {
            MultipartFile(it, mimeType ?: Files.probeContentType(it.toPath()), filename ?: it.name)
        }

        operator fun invoke(file: File, mimeType: String? = null, filename: String? = null): MultipartFile =
            MultipartFile(file, mimeType ?: Files.probeContentType(file.toPath()), filename ?: file.name)
    }
}

data class FileUrl(val url: Url) : InputFile() {
    constructor(url: String) : this(Url(url))

    override val fileId: String = "${UUID.randomUUID()}"
}


data class FileId(
    override val fileId: String
) : InputFile()
