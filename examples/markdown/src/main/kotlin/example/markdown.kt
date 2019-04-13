package telekt.examples.markdown

// NOTE: in this file 'Markdown' stands for parse mode Markdown, and 'markdown' stands for parse mode (Markdown|html)

import com.github.ajalt.clikt.core.NoRunCliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import telekt.bot.Bot
import telekt.contrib.filters.CommandFilter
import telekt.dispatcher.Dispatcher
import telekt.types.Message
import telekt.types.enums.ParseMode
import telekt.util.Recipient
import telekt.util.markdown.html
import telekt.util.markdown.htmlText
import telekt.util.markdown.markdown


class Args : NoRunCliktCommand() {
    val token: String by argument(name = "token", help = "Token of the bot")
}

fun Array<String>.parse(): Args {
    val args = Args()
    args.parse(this)
    return args
}

suspend fun main(args: Array<String>) {
    val parsedArgs = args.parse()

    val bot = Bot(parsedArgs.token, defaultParseMode = ParseMode.HTML)
    //                                                 ^^^^^^^^^^^^^^ note: default parse mode
    val dp = Dispatcher(bot)

    dp.messageHandler(CommandFilter("html")) { (message: Message) ->
        val text = html /* html 'DSL' for building html text for telegram */ {
            +"`+` adds the string to text, "
            +"note: `+` doesn't add \\n to text. You should add it explicitly, or use `nl()` function.\n" // <-- first \n
            nl() // <-- second \n

            +bold("`bold()` function makes text bold, and return it. Functions `italic()`, `code()` and `pre()` work the same way.")

            nl()

            +"There are also bold, italic, code and pre _builders_ that work like html|Markdown text builders, "
            +"but after build wraps text to bold/italic/etc"
            +pre {
                +"This is in pre block, "
                +"this is too.\n"
                +"The same."
            }

            nl(); nl()

            +"Now let's imagine that in this text you are using user input. "
            +!"User can write some < > & that can brake html, or some *, _, etc that can brake Markdown. " // Note the `!`
            +"You don't want markdown to be broken, and for this purpose there is `!` operator that escapes html or Markdown (depends on used 'DSL')\n\n"

            val input =
                message.text!! // User input. Note: this handler will be executed only if user types command '/html', so we can use `!!`.
            +"You wrote: ${!input}\n\n" // same as `escapehtml(input)`

            +"But `message.text` does NOT store markdown, markdown is stored in `message.entities`.\n"
            +"`message.mdText` and `message.htmlText` represents text with parsed entities.\n"
            +"Also `mdText` and `htmlText` automatically escapes user's text.\n"

            +"So ${italic("actually")} you wrote: ${message.htmlText}"
        }

        // Note: we doesn't pass parse mode, so default will be used (In our case -- html)
        bot.sendMessage(Recipient(message.chat.id), text)
    }

    dp.messageHandler(CommandFilter("markdown")) { (message: Message) ->
        val text = markdown {
            +"Markdown works the same way as html, but makes Markdown text. "
            +bold("Understand?")
        }
        // Note: default parse mode for this bot is html, so if we wan't to use Markdown, we need to set it explicitly
        bot.sendMessage(Recipient(message.chat.id), text, ParseMode.MARKDOWN)
    }

    dp.poll()
}