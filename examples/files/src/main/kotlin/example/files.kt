package telekt.examples.files


import com.github.ajalt.clikt.core.NoRunCliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import telekt.bot.Bot
import telekt.bot.downloadFileByFileId
import telekt.contrib.filters.ContentTypeFilter
import telekt.dispatcher.Dispatcher
import telekt.network.FileUrl
import telekt.network.InputFile
import telekt.types.Message
import telekt.types.enums.ContentType
import telekt.types.enums.ParseMode
import telekt.types.events.Event
import telekt.util.Recipient
import telekt.util.handlerregistration.*
import telekt.util.markdown.hcode
import telekt.util.markdown.hlink
import java.io.File


suspend fun main(args: Array<String>) {
    val parsedArgs = args.parse()

    val bot = Bot(parsedArgs.token, defaultParseMode = ParseMode.HTML)
    val dp = Dispatcher(bot)


    dp.dispatch {
        messages {
            handle(command("url")) { (message) ->
                // Sending file by URL
                val url = "https://cdn.discordapp.com/attachments/536882422848159784/556265112789581834/download.jpeg"
                bot.sendPhoto(
                    Recipient(message.from!!.id),
                    FileUrl(url),
                    caption = "Image from ${hlink("url", url)}"
                )
            }

            handle(command("file")) { (message) ->
                // Sending files from disk
                val file = getCatFile()
                bot.sendPhoto(
                    Recipient(message.from!!.id),
                    InputFile(file),
                    caption = "Image from ${hcode(file.path)}"
                )
            }

            handle(photo, text("fileId")) { (message) ->
                // Sending file by [fileId]
                // First file id is the smallest one, and last - largest one (original)
                val fileId = message.photo!!.last().fileId
                bot.sendPhoto(
                    Recipient(message.from!!.id),
                    InputFile(fileId),
                    caption = "Image from fileId=$fileId"
                )
            }

            handle(photo, text("download")) { (message) ->
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

val <E> HandlerDSL<E>.photo: ContentTypeFilter<E>
        where E : Event<Message>
    get() = ContentTypeFilter(ContentType.PHOTO)