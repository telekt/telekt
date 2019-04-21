package rocks.waffle.telekt.network.ktor.callfactory

import io.ktor.client.HttpClient
import io.ktor.client.call.HttpClientCall
import io.ktor.client.call.call
import io.ktor.client.request.accept
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.content.TextContent
import kotlinx.serialization.json.Json
import rocks.waffle.telekt.network.requests.abstracts.SimpleRequest


class SimpleCallFactory : KtorCallFactory<Any, SimpleRequest<Any>> {
    private val json = Json(encodeDefaults = false)

    override suspend fun <R : SimpleRequest<Any>> prepareCall(client: HttpClient, baseUrl: String, request: R): HttpClientCall =
        client.call {
            url("$baseUrl/${request.method.apiName}")
            method = HttpMethod.Post

            accept(ContentType.Application.Json)

            val content = request.stringify(json)

            body = TextContent(
                content,
                ContentType.Application.Json
            )
            build()
        }
}

