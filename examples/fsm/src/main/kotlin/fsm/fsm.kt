package telekt.examples.fsm


import telekt.contrib.storages.ActorMemoryStorage
import telekt.dispatcher.Dispatcher
import telekt.fsm.next
import telekt.types.KeyboardButton
import telekt.types.ReplyKeyboardMarkup
import telekt.types.ReplyKeyboardRemove
import telekt.types.enums.ParseMode
import telekt.util.handlerregistration.*
import telekt.util.markdown.html


suspend fun main(args: Array<String>) {
    val parsedArgs = args.parse()
    val dp = Dispatcher(parsedArgs.token, ActorMemoryStorage())
    val db = DataBase()
    val bot = dp.bot

    dp.dispatch {
        messages {
            handle(command("start")) { (message, ctx) ->
                // set state
                ctx.setState(States.NAME)

                bot.replyTo(message, "Hi there! What's your name?")
            }

            handle(command("canceled")) { (message, ctx) ->
                // Cancel state and inform user about it
                ctx.finish()
                // And remove keyboard (just in case)
                bot.replyTo(message, "Cenceled.", replyMarkup = ReplyKeyboardRemove())
            }

            handle(state(States.NAME)) { (message, ctx) ->
                if (message.text == null) {
                    bot.answerOn(message, "Name gotta be a text. What's your name?")
                } else {
                    db.setName(message.from!!.id, message.text!!)
                    ctx.next()

                    bot.answerOn(message, "Ok. How old are you?")
                }
            }

            // Save age (age gotta be digit)
            handle(isDigit, state(States.AGE)) { (message, ctx) ->
                db.setAge(message.from!!.id, message.text!!.toInt())
                ctx.next()

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

            handle(state(States.GENDER)) { (message, ctx) ->
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

                    ctx.finish() // Finish conversation
                }
            }
        }
    }

    dp.poll()
}
