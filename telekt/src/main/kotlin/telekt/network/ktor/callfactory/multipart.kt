package rocks.waffle.telekt.network.ktor.callfactory

import io.ktor.client.HttpClient
import io.ktor.client.call.HttpClientCall
import io.ktor.client.call.call
import io.ktor.client.request.accept
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import kotlinx.io.streams.asInput
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import rocks.waffle.telekt.network.requests.abstracts.MultipartRequest


class MultipartRequestCallFactory : KtorCallFactory<Any, MultipartRequest<*>> {
    private val json = Json(encodeDefaults = false)

    override suspend fun <R : MultipartRequest<*>> prepareCall(client: HttpClient, baseUrl: String, request: R): HttpClientCall =
        client.call {
            url("$baseUrl/${request.method.apiName}")
            method = HttpMethod.Post
            accept(ContentType.Application.Json)
            body = MultiPartFormDataContent(
                formData {
                    request.paramsJson(json).jsonObject.forEach { (key, value) ->
                        // TODO: split common multipart request and request for media group, or clear that place with other method....
                        if (!(!request.attach && key in request.mediaMap.keys) && !value.isNull) {
                            if (value is JsonPrimitive) append(key, value.content)
                            else append(key, value.toString())
                        }
                    }

                    for ((key, value) in request.mediaMap) {
                        append(
                            key,
                            value.file.inputStream().asInput(),
                            Headers.build {
                                append(HttpHeaders.ContentType, value.mimeType)
                                append(HttpHeaders.ContentDisposition, "filename=${value.filename}")
                            }
                        )
                    }


                }
            )

            build()
        }
}
