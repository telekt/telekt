package rocks.waffle.telekt.types.enums

enum class ContentType {
    TEXT, // text
    AUDIO, // audio
    DOCUMENT, // document
    ANIMATION, // animation
    GAME, // game
    PHOTO, // photo
    STICKER, // sticker
    VIDEO, // video
    VIDEO_NOTE, // video_note
    VOICE, // voice
    CONTACT, // contact
    LOCATION, // location
    VENUE, // venue
    NEW_CHAT_MEMBERS, // new_chat_member
    LEFT_CHAT_MEMBER, // left_chat_member
    INVOICE, // invoice
    SUCCESSFUL_PAYMENT, // successful_payment
    CONNECTED_WEBSITE, // connected_website
    MIGRATE_TO_CHAT_ID, // migrate_to_chat_id
    MIGRATE_FROM_CHAT_ID, // migrate_from_chat_id
    PINNED_MESSAGE, // pinned_message
    NEW_CHAT_TITLE, // new_chat_title
    NEW_CHAT_PHOTO, // new_chat_photo
    DELETE_CHAT_PHOTO, // delete_chat_photo
    GROUP_CHAT_CREATED, // group_chat_created
    PASSPORT_DATA, // passport_data
    POLL, // poll

    UNKNOWN // unknown
}