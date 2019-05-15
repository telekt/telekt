package rocks.waffle.telekt.network

interface Api {
    fun url(token: String, method: TelegramMethod): String
    fun fileUrl(token: String, path: String): String
}

object DefaultApi : Api {
    override fun url(token: String, method: TelegramMethod): String = "https://api.telegram.org/bot$token/${method.apiName}"
    override fun fileUrl(token: String, path: String): String = "https://api.telegram.org/file/bot$token/$path"
}