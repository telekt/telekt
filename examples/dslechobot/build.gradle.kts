group = "rocks.waffle.telekt.examples.dslechobot"
version = "0.1.0"

plugins { application }

application { mainClassName = "rocks.waffle.telekt.examples.dslechobot.EchobotKt" }

repositories { maven("https://kotlin.bintray.com/kotlinx") }

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.2.0-alpha")

    // logging
    implementation("org.slf4j:slf4j-api:1.6.1")
    implementation("ch.qos.logback:logback-classic:1.2.3")

    // telekt itself
    implementation(project(":telekt"))

    // for parsing command line args
    implementation("com.github.ajalt:clikt:1.6.0")
}
