group = "rocks.waffle.telekt"
version = "0.0.1"

val kotlinVersion = "1.3.21"
val ktorVersion = "1.1.3"
val kotlinxIoVersion = "0.1.0"

buildscript {
    repositories { jcenter() }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.21") // TODO: version to val
        classpath("org.jetbrains.kotlin:kotlin-serialization:1.3.21")
    }
}

apply {
    plugin("kotlinx-serialization")
}

repositories {
    mavenCentral()
    maven("https://kotlin.bintray.com/kotlinx")
    maven("https://dl.bintray.com/kotlin/kotlinx")
    maven("https://dl.bintray.com/kotlin/ktor")
}

dependencies {
    // kotlinx serialization
    api("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.10.0")

    // coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.2.0-alpha")

    // Ktor (http work)
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-client-apache:$ktorVersion")
    implementation("io.ktor:ktor-html-builder:$ktorVersion")
    implementation("io.ktor:ktor-network-tls:$ktorVersion")

    // logging
    implementation("io.github.microutils:kotlin-logging:1.6.20")

    // mb for ktor
    implementation("org.jetbrains.kotlinx:kotlinx-io-jvm:$kotlinxIoVersion")
    
    // User friendly time calculation
    implementation("com.github.kizitonwose:time:1.0.2")

    // reflection (for exceptions)
    implementation("org.reflections:reflections:0.9.11")
}
