package rocks.waffle.telekt.examples.fsm


import rocks.waffle.telekt.bot.Bot
import rocks.waffle.telekt.contrib.storages.ActorMemoryStorage
import rocks.waffle.telekt.dispatcher.Dispatcher
import rocks.waffle.telekt.fsm.next
import rocks.waffle.telekt.types.KeyboardButton
import rocks.waffle.telekt.types.ReplyKeyboardMarkup
import rocks.waffle.telekt.types.ReplyKeyboardRemove
import rocks.waffle.telekt.types.enums.ParseMode
import rocks.waffle.telekt.util.answerOn
import rocks.waffle.telekt.util.handlerregistration.*
import rocks.waffle.telekt.util.markdown.html
import rocks.waffle.telekt.util.replyTo


suspend fun main(args: Array<String>) {
    val parsedArgs = args.parse()
    val bot = Bot(parsedArgs.token)
    val dp = Dispatcher(bot, ActorMemoryStorage())
    val db = DataBase()

    dp.dispatch {
        messages {
            handle(command("start")) { message ->
                // set state
                fsmContext.setState(States.NAME)

                bot.replyTo(message, "Hi there! What's your name?")
            }

            handle(command("canceled")) { message ->
                // Cancel state and inform user about it
                fsmContext.finish()
                // And remove keyboard (just in case)
                bot.replyTo(message, "Cenceled.", replyMarkup = ReplyKeyboardRemove())
            }

            handle(state(States.NAME)) { message ->
                if (message.text == null) {
                    bot.answerOn(message, "Name gotta be a text. What's your name?")
                } else {
                    db.setName(message.from!!.id, message.text!!)
                    fsmContext.next()

                    bot.answerOn(message, "Ok. How old are you?")
                }
            }

            // Save age (age gotta be digit)
            handle(isDigit, state(States.AGE)) { message ->
                db.setAge(message.from!!.id, message.text!!.toInt())
                fsmContext.next()

                val markup = ReplyKeyboardMarkup(
                    arrayOf(KeyboardButton("Male"), KeyboardButton("Female")),
                    arrayOf(KeyboardButton("Other"))
                )

                bot.replyTo(message, "What is your gender?", replyMarkup = markup)
            }

            // State is AGE, but user writed not digit
            handle(state(States.AGE)) {
                bot.replyTo(it, "Age gotta be a number.\nHow old are you? (digits only)")
            }

            handle(state(States.GENDER)) { message ->
                val gender = parseGender(message.text) // text in [genders], so it's string

                if (gender == Gender.PARSE_ERROR) {
                    bot.replyTo(message, "Gender gotta be one of: male, female, other. What is your gender? (genders only)")
                } else {
                    db.setGender(message.from!!.id, gender)
                    val user = db.getUser(message.from!!.id)


                    val markup = ReplyKeyboardRemove()

                    val text = html {
                        val name = user.name?.let(::bold) ?: italic("ERROR: Name is null")
                        val age = user.age?.toString() ?: italic("ERROR: Age is null")
                        val userGender = user.gender?.toString() ?: italic("ERROR: Gender is null")

                        +"Hi! Nice to meet you, $name!\n"
                        +"Age: $age\n"
                        +"Gender: $userGender\n"
                    }

                    bot.replyTo(message, text, parseMode = ParseMode.HTML, replyMarkup = markup)

                    fsmContext.finish() // Finish conversation
                }
            }
        }
    }

    dp.poll()
}
