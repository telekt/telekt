### TeleKt

Easy to use, asynchronous wrapper for the [Telegram Bot API](https://core.telegram.org/bots/api") written in pure Kotlin. Inspired by [aiogram](https://github.com/aiogram/aiogram).

![logo](resources/logo.svg)

Table of content:
  * [Getting started](#getting-started)
  * [Writing your first bot](#writing-your-first-bot)
    * [Prerequisites](#prerequisites)
    * [A simple echo bot](#a-simple-echo-bot)
  * [General API Documentation](#general-api-documentation)
    * [Types](#types)
    * [Methods](#methods)
    * [General use of the API](#general-use-of-the-api)
      * [Message handlers](#message-handlers)
      * [bot.me](#bot.me)
      * [Reply markup](#reply-markup)
      * [Inline Mode](#inline-mode)
      * [Working with entities](#working-with-entities)
  * [Advanced use of the API](#advanced-use-of-the-api)
    * [Sending large text messages](#sending-large-text-messages)
    * [Using web hooks](#using-web-hooks)
    * [Logging](#logging)
  * [The Telegram Chat Group](#the-telegram-chat-groups-and-channel)
  * [Examples](#Examples)
  * [Bots using this API](#bots-using-this-api)
  * [Wrapping Notes](#wrapping-notes)
  * [TODO](#todo)
  * [Special thanks to](#special-thanks-to)

## Getting started.

Install to your project:

* Using Gradle.kts:
```kotlin
implementation("rocks.waffle.telekt:telekt:0.2.0")
```
Also this project uses [kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization), so you need to add kotlinx repository: 
```kotlin
repositories {
    maven("https://kotlin.bintray.com/kotlinx")
}
```

_**Note**: While API is ready-to-use, it is still in **beta**. Any new version **can** break backward compatibility, see change logs and be careful._

## Writing your first bot

### Prerequisites

It is presumed that you [have obtained an API token with @BotFather](https://core.telegram.org/bots#botfather). We will call this token `TOKEN`.
Furthermore, you have basic knowledge of the Kotlin programming language and more importantly [the Telegram Bot API](https://core.telegram.org/bots/api).

### A simple echo bot

TeleKt splits **calling tg api methods** and **dispatching incoming updates**.    

* For first feature there is class [Bot](telekt/src/main/kotlin/telekt/bot/bot.kt) that encapsulates all API calls in a single class.  
* For second — class [Dispatcher](telekt/src/main/kotlin/telekt/dispatcher/dispatcher.kt) provide several ways to listen for incoming updates (e.g. messages). 

Create a file called `echobot.kt`.
Then, open the file and create an instance of the `Bot` and `Dispatcher` classes.
```kotlin
import rocks.waffle.telekt.bot.*
import rocks.waffle.telekt.dispatcher.*

fun main() {
    val bot = Bot("TOKEN")
    val dp = Dispatcher(bot)
}
```
_Note: Make sure to actually replace TOKEN with your own API token._

After that declaration, we need to register some so-called message handlers. Message handlers define filters which a message must pass. If a message passes the filter, the passed function is called and the incoming message is passed as an argument.

Let's define a message handler which handles incoming `/start` and `/help` commands.
```kotlin
dp.messageHandler(CommandFilter("start", "help")) { message: MessageEvent ->
    message.answer("Howdy, how are you doing?")
}
```

Let's add another handler:
```kotlin
dp.messageHandler { message ->
    message.answer(message.text ?: "this message has no text")
}
```
This one echoes all incoming text messages back to the sender. We doesn't pass any filters, so **all** messages will income here.  
*Note: all handlers are tested in the order in which they were declared*

We now have a basic bot which replies a static message to "/start" and "/help" commands and which echoes the rest of the sent messages. To start the bot, add the following to our source file:
```kotlin
dp.poll()
```

Alright, that's it! Our source file now looks like this:
```kotlin
import rocks.waffle.telekt.bot.*
import rocks.waffle.telekt.dispatcher.*
import rocks.waffle.telekt.types.events.MessageEvent
import rocks.waffle.telekt.contrib.filters.CommandFilter


suspend fun main() {
// ^^^^ NOTE: this lib is async, so you need to run it from suspending funciton
    val bot = Bot("TOKEM")
    val dp = Dispatcher(bot)

    dp.messageHandler(CommandFilter("start", "help")) { message: MessageEvent ->
        message.answer("Hi there 0/")
    }

    dp.messageHandler { message ->
        message.answer(message.text ?: "this message has no text")
    }

    dp.poll()
}

```
After running bot, test it by sending commands ('/start' and '/help') and arbitrary text messages.  
Full example you can see at [there](examples/echobot)

## General API Documentation

### Types

All types are defined in [telekt.types](telekt/src/main/kotlin/telekt/types) package.  
They are all completely in line with the [Telegram API's definition of the types](https://core.telegram.org/bots/api#available-types), except that all field renamed from `snake_case` to `camelCase` (like `message.message_id` => `message.messageId`). Thus, attributes such as `messageId` can be accessed directly with `message.messageId`. 

The Message class also has a `contentType` attribute, which defines the type of the Message. 

### Methods

All [API methods](https://core.telegram.org/bots/api#available-methods) are located in the [Bot](telekt/src/main/kotlin/rocks.waffle.telekt/bot/bot.kt) class. 

### General use of the API

Outlined below are some general use cases of the API.

#### Message handlers
A message handler is a function that is given to `messageHandler` function of a [Dispatcher](telekt/src/main/kotlin/telekt/dispatcher/dispatcher.kt) instance.  
Message handlers consist of 0, one or multiple filters.
Each filter's `test(...)` function must return `True` for a certain message in order for a message handler to become eligible to handle that message. A message handler is declared in the following way:
```kotlin
dp.messageHandler(*filters) { /* ... */ }

fun functionName(message: MessageEvent) { /* ... */ }

dp.messageHandler(*filters, block = ::functionName)
```
`functionName` is not bound to any restrictions. Any function name is permitted with message handlers. The function must accept at most one argument, which will be the message event that the function must handle.
`filters` is a vararg array of [Filter](telekt/src/main/kotlin/telekt/dispatcher/handler.kt)s.
One handler may have multiple filters.

There is also `DSL`-like builder for registration handlers:
```kotlin
dp.dispatch {
    messages {
        handle(/* filters here */) { /* message handler here */ }
    }
    
    callbackQuerys { 
        handle(/* filters here */) { /* callback query handler here */ }
    }
    
    // and so on
}
``` 

**Important: all handlers are tested in the order in which they were declared**

All other handlers (callbackQuery, editedMessage, etc) work the same way.


#### Bot.me
[Bot](telekt/src/main/kotlin/telekt/bot/bot.kt).me is lazy started coroutine builded with `async{}` builder that just calls `bot.getMe()`. Only bot creator can change bot user (via [BotFather](https://t.me/BotFather)) so in most cases it's safe to use it.
```kotlin
bot.me.await()
```
#### Reply markup
All `send<Something>` functions of [Bot](telekt/src/main/kotlin/telekt/bot/bot.kt) take an optional `replyMarkup` argument.  
This argument must be an instance of `ReplyKeyboardMarkup`, `InlineKeyboardMarkup`, `ReplyKeyboardRemove` or `ForceReply`, which are defined in [markup.kt](telekt/src/main/kotlin/telekt/types/replymarkup/markup.kt).

### Inline Mode

More information about [Inline mode](https://core.telegram.org/bots/inline).

Refer [Bot Api](https://core.telegram.org/bots/api#messageentity) for extra details

## Advanced use of the API

// TODO: write about dispatch modes (need to be implemented)

### Sending large text messages
Sometimes you must send messages that exceed 5000 characters. The Telegram API can not handle that many characters in one request, so we need to split the message in multiples. Here is how to do that using the API:
```kotlin
import rocks.waffle.telekt.util.splitByLenght

val largeText = "Really large text here"

// Split the text each 3000 characters.
// splitByLenght returns a list with the splitted text.
val texts = largeText.splitByLenght() // you can also pass different max lenght
texts.forEach { bot.sendMessage(chat_id, it) }
```

### Using web hooks

Webhooks is not implemented yet :(

### Logging

This lib is using [KotlinLogging](https://github/com/MicroUtils/kotlin-logging). 
In examples we are using [logback](https://logback.qos.ch).  

## F.A.Q.

No questions there yet, ask me something! :)


## The Telegram Chat Group(s) (and channel)

Get help. Discuss. Chat.

* Join the [TeleKt Telegram Chat Group](https://t.me/telektlib)
* Or join [Russian TeleKt Chat Group](https://t.me/telektru)
* Also join our [News Channel](https://t.me/telektnews)

## Examples

All examples are located in [examples](examples) directory
* [Echo bot](examples/echobot)
* [Dsl echo bot](examples/dslechobot)
* [Files](examples/files)
* [FSM](examples/fsm)
* [Inline buttons](examples/inlineButtons)
* [Keyboard](examples/keyboard)
* [Markdown](examples/markdown)

## Bots using this API
No one yet, you can become first! 
Send a telegram message to [@wafflelapkin](https://t.me/wafflelapkin), 
or write in our [group](#the-telegram-chat-groups-and-channel),
or open an issue on [github](https://github.com/telekt/telekt/issues).

## Wrapping Notes
Note some things about tg bot api wrapping:

1. All methods that return `True` in tg bot api (like [unbanChatMember](https://core.telegram.org/bots/api#unbanchatmember)), in TeleKt return `Unit`
2. All [`edit*`](https://core.telegram.org/bots/api#updating-messages) methods splited in 2 overloads:
   1. For messages sent by the bot (return [`Message`](telekt/src/main/kotlin/telekt/types/common/Message.kt))
   2. For inline messages (return `Unit`)
3. All names have been changed to match with coding conventions
4. Some methods that accept const strings in tg bot api (like [sendChatAction](https://core.telegram.org/bots/api#sendchataction)), in TeleKt accept enums
5. All `chatId` params is of type [`Recipient`](telekt/src/main/kotlin/telekt/util/Recipient.kt) cause Kotlin haven't algebraic types.

## TODO

Things between 'now' and 'release 1.0'

* [ ] Webhooks
  - [ ] Receiving updates
  - [ ] Answer into webhook
* [ ] Middlewares (like in [aiogram](https://github.com/aiogram/aiogram))
  - [ ] Implement middlewares
  - [ ] Write some built-in middlewares
    + [ ] timeit
    + [ ] logging
    + [ ] i18n (?)
* [ ] Add more examples
* [ ] Docs
* [ ] Write more comments in code
  - [ ] Write comments in telegram types
  - [ ] Write comments everywhere else  
* [ ] Add more storages
  - [ ] Mongo db
  - [ ] PostrgeSQL
* [ ] Safe update dispatching (with different `Dispatcher`?)
* [ ] Anti-spam and/or api request limits handling
  
 

## Special thanks to
* [gt22](https://github.com/gt22) and [mrAppleXZ](https://github.com/mrAppleXZ) from pearx team — for answering my stupid questions
* Russian [Kotlin Community](https://t.me/kotlin_lang) in telegram — also for answering my stupid questions
* [Alex Root Junior](https://github.com/JrooTJunior) — for writing [aiogram](https://github.com/aiogram/aiogram)
* [Dmitriy Shilnikov](https://t.me/dshilnikov) — for fixing jackson deserialization (Actually now TeleKt use kotlinx.serialization instead of jackson, but anyway thanks)
* [afdw](https://github.com/afdw) — for cleaning `logo.svg` 
* My parents — for love and support :heart:
