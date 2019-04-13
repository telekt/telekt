plugins {
    `build-scan`
    `maven-publish`
    kotlin("jvm") version "1.3.21"
}

allprojects {
    apply(plugin = "java")
    repositories {
        jcenter()
        maven("https://jitpack.io") // for 'https://github.com/kizitonwose/Time'
    }
}

allprojects {
    tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class).all {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    dependencies {
        implementation(kotlin("stdlib-jdk8"))
    }
}

buildScan {
    termsOfServiceUrl = "https://gradle.com/terms-of-service"
    termsOfServiceAgree = "yes"

    publishAlways()
}
