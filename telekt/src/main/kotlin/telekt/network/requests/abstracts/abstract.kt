package rocks.waffle.telekt.network.requests.abstracts

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import rocks.waffle.telekt.network.InputFile
import rocks.waffle.telekt.network.MultipartFile
import rocks.waffle.telekt.network.TelegramMethod

sealed class Request<T : Any> {
    /** Name of telegram method */
    abstract val method: TelegramMethod

    /** Serializer for result */
    abstract val resultDeserializer: KSerializer<out T>
}

abstract class MultipartRequest<T : Any> : Request<T>() {
    abstract fun paramsJson(json: Json): JsonElement
    abstract val mediaMap: Map<String, MultipartFile>
    open val attach: Boolean = false
}

fun Pair<String, InputFile?>.asMediaMap(): Map<String, MultipartFile?> = when (second) {
    is MultipartFile? -> mapOf(first to second as MultipartFile?)
    else -> mapOf()
}

fun Map<String, InputFile?>.asMediaMap(): Map<String, MultipartFile> = mapNotNull {
    when (it.value) {
        is MultipartFile -> it.key to it.value as MultipartFile
        else -> null
    }
}.toMap()


abstract class SimpleRequest<T : Any> : Request<T>(), SelfSerializable


interface SelfSerializable {
    fun stringify(json: Json): String
}
