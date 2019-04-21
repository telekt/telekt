group = "rocks.waffle.telekt"
version = "0.1.0"

val kotlinVersion = "1.3.21"
val ktorVersion = "1.1.3"
val kotlinxIoVersion = "0.1.0"

val uploadUsernameProp: String? by project
val uploadPasswordProp: String? by project
val releaseBuild: String? by project


buildscript {
    repositories { jcenter() }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.21") // TODO: version to val
        classpath("org.jetbrains.kotlin:kotlin-serialization:1.3.21")
    }
}

plugins {
    `maven-publish`
    signing
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

tasks.register<Jar>("sourcesJar") {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

tasks.register<Jar>("javadocJar") {
    archiveClassifier.set("javadoc")
    from(tasks.javadoc.get().destinationDir)
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            artifact(tasks["sourcesJar"])
            artifact(tasks["javadocJar"])

            groupId = "rocks.waffle.telekt"
            artifactId = "telekt"

            pom {
                name.set("TeleKt - kotlin tg bot api lib")
                description.set("Easy to use, asynchronous wrapper for the Telegram Bot API written in pure Kotlin.")
                url.set("https://github.com/telekt/telekt")

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/mit-license.php")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/telekt/telekt.git")
                    developerConnection.set("scm:git:ssh://github.com/telekt/telekt.git")
                    url.set("http://github.com/telekt/telekt/")
                }
                developers {
                    developer {
                        id.set("wafflelapkin")
                        name.set("Waffle Lapkin")
                        email.set("waffle.lapkin@gmail.com")
                    }
                }

            }
        }
    }
    repositories {
        maven {
            credentials {
                username = uploadUsernameProp
                password = uploadPasswordProp

                username ?: println("Upload USERNAME not set")
                password ?: println("Upload PASSWORD not set")
            }


            val releasesRepoUrl = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2")
            val snapshotsRepoUrl = uri("https://oss.sonatype.org/content/repositories/snapshots/")
            url = if (version.toString().endsWith("SNAPSHOT")) {
                println("snapshot")
                snapshotsRepoUrl
            } else releasesRepoUrl
        }
    }
}

val archivesBaseName = "telekt"

signing {
    releaseBuild?.toBoolean()?.let {
        if (it) {
            sign(publishing.publications["mavenJava"])
        }
    }
}
