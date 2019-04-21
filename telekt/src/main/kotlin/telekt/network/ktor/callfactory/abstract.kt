package rocks.waffle.telekt.network.ktor.callfactory

import io.ktor.client.HttpClient
import io.ktor.client.call.HttpClientCall
import rocks.waffle.telekt.network.requests.abstracts.Request

interface KtorCallFactory<out T : Any, RB : Request<T>> {
    suspend fun <R> prepareCall(client: HttpClient, baseUrl: String, request: R): HttpClientCall where R : RB
}
