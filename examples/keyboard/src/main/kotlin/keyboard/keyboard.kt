package rocks.waffle.telekt.examples.keyboard

/*
 * Example of sending custom keyboards
 */

import rocks.waffle.telekt.dispatcher.Dispatcher
import rocks.waffle.telekt.types.ReplyKeyboardRemove
import rocks.waffle.telekt.util.Recipient
import rocks.waffle.telekt.util.ReplyKeyboardMarkup
import rocks.waffle.telekt.util.handlerregistration.command
import rocks.waffle.telekt.util.handlerregistration.dispatch
import rocks.waffle.telekt.util.handlerregistration.handle
import rocks.waffle.telekt.util.handlerregistration.messages


suspend fun main(args: Array<String>) {
    val parsedArgs = args.parse()
    val dp = Dispatcher(parsedArgs.token)
    val bot = dp.bot

    dp.dispatch {
        messages {

            handle(command("one_time")) {
                /*
                 * `oneTimeKeyboard = true` Requests clients to hide the keyboard as soon as it's been used.
                 * The keyboard will still be available, but clients will automatically display the usual letter-keyboard in the chat –
                 * the user can press a special button in the input field to see the custom keyboard again. Defaults to false.
                 */
                val markup = ReplyKeyboardMarkup(oneTimeKeyboard = true) {
                    add(button("test"), button("text"))
                }
                bot.replyTo(it, "You can use this keyboard only once", replyMarkup = markup)
            }

            handle(command("resize")) {
                /*
                 * `resizeKeyboard = true` requests clients to resize the keyboard vertically for optimal fit
                 * (e.g., make the keyboard smaller if there are just two rows of buttons).
                 * Defaults to false, in which case the custom keyboard is always of the same height as the app's standard keyboard.
                 */
                val markup = ReplyKeyboardMarkup(resizeKeyboard = true) {
                    add(button("test"), button("text"))
                }
                bot.replyTo(it, "This keyboard will be resized vertically for optimal fit", replyMarkup = markup)
            }

            /*
             * there is also `selective` param in ReplyKeyboardMarkup, but it's to complicated, for this example.
             * See docs for more: https://core.telegram.org/bots/api#replykeyboardmarkup
             */

            handle(command("remove")) {
                /*
                 * `ReplyKeyboardRemove` removes the keyboard.
                 */
                bot.replyTo(it, "keyboard removed", replyMarkup = ReplyKeyboardRemove())
            }
        }
    }

    bot.sendMessage(
        Recipient(218485655),
        "text"
    )

    dp.poll()
}
