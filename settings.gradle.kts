rootProject.name = "rocks.waffle.telekt"

/*
 * rocks.waffle.telekt
 * │
 * ├─ lib       // TeleKt lib itself
 * │
 * └─ examples  // lib usage examples
 *    │
 *    ├─ echobot        // simple echo bot that just echos your messages
 *    │
 *    ├─ dslechobot     // same as echobot, but written using dsl
 *    │
 *    ├─ markdown       // example that shows how to work with markdown
 *    │
 *    ├─ fsm            // finite state machine usage
 *    │
 *    ├─ keyboard       // working with keyboard
 *    │
 *    ├─ inlineButtons  // working with inline buttons (callback querys)
 *    │
 *    └─ files          // downloading and uploading files to/from telegram
 */
include (
        ":lib",
        ":examples:echobot",
        ":examples:dslechobot",
        ":examples:markdown",
        ":examples:fsm",
        ":examples:keyboard",
        ":examples:inlineButtons",
        ":examples:files"
)