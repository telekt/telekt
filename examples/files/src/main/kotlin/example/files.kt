package rocks.waffle.telekt.examples.files


import com.github.ajalt.clikt.core.NoRunCliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import rocks.waffle.telekt.bot.Bot
import rocks.waffle.telekt.bot.downloadFileByFileId
import rocks.waffle.telekt.contrib.filters.ContentTypeFilter
import rocks.waffle.telekt.dispatcher.Dispatcher
import rocks.waffle.telekt.network.FileUrl
import rocks.waffle.telekt.network.InputFile
import rocks.waffle.telekt.types.Message
import rocks.waffle.telekt.types.enums.ContentType
import rocks.waffle.telekt.types.enums.ParseMode
import rocks.waffle.telekt.util.Recipient
import rocks.waffle.telekt.util.handlerregistration.*
import rocks.waffle.telekt.util.markdown.hcode
import rocks.waffle.telekt.util.markdown.hlink
import java.io.File


suspend fun main(args: Array<String>) {
    val parsedArgs = args.parse()

    val bot = Bot(parsedArgs.token, defaultParseMode = ParseMode.HTML)
    val dp = Dispatcher(bot)


    dp.dispatch {
        messages {
            handle(command("url")) { message ->
                // Sending file by URL
                val url = "https://cdn.discordapp.com/attachments/536882422848159784/556265112789581834/download.jpeg"
                bot.sendPhoto(
                    Recipient(message.from!!.id),
                    FileUrl(url),
                    caption = "Image from ${hlink("url", url)}"
                )
            }

            handle(command("file")) { message ->
                // Sending files from disk
                val file = getCatFile()
                bot.sendPhoto(
                    Recipient(message.from!!.id),
                    InputFile(file),
                    caption = "Image from ${hcode(file.path)}"
                )
            }

            handle(photo, text("fileId")) { message ->
                // Sending file by [fileId]
                // First file id is the smallest one, and last - largest one (original)
                val fileId = message.photo!!.last().fileId
                bot.sendPhoto(
                    Recipient(message.from!!.id),
                    InputFile(fileId),
                    caption = "Image from fileId=$fileId"
                )
            }

            handle(photo, text("download")) { message ->
                val path = System.getProperty("user.home") + "/photo.jpg"
                val fileId = message.photo!!.last().fileId
                // Download file
                bot.downloadFileByFileId(fileId, path)

                // And sending it back
                bot.sendMessage(
                    Recipient(message.from!!.id),
                    "Successfully downloaded image to ${hcode(path)}"
                )
            }
        }
    }

    dp.poll()
}

class Args : NoRunCliktCommand() {
    val token: String by argument(name = "token", help = "Token of the bot")
}

fun Array<String>.parse(): Args {
    val args = Args()
    args.parse(this)
    return args
}

class R

fun getCatFile(): File = File(R::class.java.classLoader.getResource("cat.jpg").file)

val @Suppress("UNUSED") HandlerDSL<Message>.photo: ContentTypeFilter get() = ContentTypeFilter(ContentType.PHOTO)
