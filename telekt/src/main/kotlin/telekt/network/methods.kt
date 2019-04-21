package rocks.waffle.telekt.network

enum class TelegramMethod(val apiName: String) {
    // Getting Updates
    getUpdates("getUpdates"),
    setWebhook("setWebhook"),
    deleteWebhook("deleteWebhook"),
    getWebhookInfo("getWebhookInfo"),

    // Available requests
    getMe("getMe"),
    sendMessage("sendMessage"),
    forwardMessage("forwardMessage"),
    sendPhoto("sendPhoto"),
    sendAudio("sendAudio"),
    sendDocument("sendDocument"),
    sendVideo("sendVideo"),
    sendAnimation("sendAnimation"),
    sendVoice("sendVoice"),
    sendVideoNote("sendVideoNote"),
    sendMediaGroup("sendMediaGroup"),
    sendLocation("sendLocation"),
    editMessageLiveLocation("editMessageLiveLocation"),
    stopMessageLiveLocation("stopMessageLiveLocation"),
    sendVenue("sendVenue"),
    sendContact("sendContact"),
    sendChatAction("sendChatAction"),
    getUserProfilePhotos("getUserProfilePhotos"),
    getFile("getFile"),
    kickChatMember("kickChatMember"),
    unbanChatMember("unbanChatMember"),
    restrictChatMember(
        "restrictChatMember"
    ),
    promoteChatMember("promoteChatMember"),
    exportChatInviteLink("exportChatInviteLink"),
    setChatPhoto("setChatPhoto"),
    deleteChatPhoto("deleteChatPhoto"),
    setChatTitle("setChatTitle"),
    setChatDescription("setChatDescription"),
    pinChatMessage("pinChatMessage"),
    unpinChatMessage("unpinChatMessage"),
    leaveChat("leaveChat"),
    getChat("getChat"),
    getChatAdministrators("getChatAdministrators"),
    getChatMembersCount("getChatMembersCount"),
    getChatMember("getChatMember"),
    setChatStickerSet("setChatStickerSet"),
    deleteChatStickerSet("deleteChatStickerSet"),
    answerCallbackQuery("answerCallbackQuery"),

    // Updating messages
    editMessageText("editMessageText"),
    editMessageCaption("editMessageCaption"),
    editMessageMedia("editMessageMedia"),
    editMessageReplyMarkup("editMessageReplyMarkup"),
    deleteMessage("deleteMessage"),

    // Stickers
    sendSticker("sendSticker"),
    getStickerSet("getStickerSet"),
    uploadStickerFile("uploadStickerFile"),
    createNewStickerSet("createNewStickerSet"),
    addStickerToSet("addStickerToSet"),
    setStickerPositionInSet("setStickerPositionInSet"),
    deleteStickerFromSet("deleteStickerFromSet"),

    // Inline mode
    answerInlineQuery("answerInlineQuery"),

    // Payments
    sendInvoice("sendInvoice"),
    answerShippingQuery("answerShippingQuery"),
    answerPreCheckoutQuery("answerPreCheckoutQuery"),

    setPassportDataErrors("setPassportDataErrors"),

    // Games
    sendGame("sendGame"),
    setGameScore("setGameScore"),
    getGameHighScores("getGameHighScores"),

    // Pools
    sendPoll("sendPoll"),
    stopPoll("stopPoll")
}