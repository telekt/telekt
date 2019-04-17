package rocks.waffle.telekt.types.enums

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import rocks.waffle.telekt.util.enumByValueSerializer

@Serializable(with = MessageEntityType.S::class) enum class MessageEntityType(val apiName: String) {
    MENTION("mention"), // mention - @username
    HASHTAG("hashtag"), // hashtag
    CASHTAG("cashtag"), // cashtag
    BOT_COMMAND("bot_command"), // bot_command
    URL("url"), // url
    EMAIL("email"), // email
    PHONE_NUMBER("phone_number"), // phone_number
    BOLD("bold"), // bold -  bold text
    ITALIC("italic"), // italic -  italic text
    CODE("code"), // code -  monowidth string
    PRE("pre"), // pre -  monowidth block
    TEXT_LINK("text_link"), // text_link -  for clickable text URLs
    TEXT_MENTION("text_mention"); // text_mention -  for users without usernames

    @Serializer(forClass = MessageEntityType::class)
    object S : KSerializer<MessageEntityType> by enumByValueSerializer({ apiName })
}