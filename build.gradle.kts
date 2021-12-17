plugins {
    kotlin("jvm") version "1.6.0"
    kotlin("plugin.serialization") version "1.5.31"
    java
}

group = "net.savagelabs"
description = "A simple staff plugin"
version = "1.0-STABLE"

val targetJavaVersion = "17"

repositories {
    mavenCentral()

    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://repo.mattstudios.me/artifactory/public/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.18-R0.1-SNAPSHOT")

    compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.1")
    compileOnly("org.jetbrains.kotlin:kotlin-stdlib:1.6.0")

    api("net.kyori:adventure-api:4.9.3")

    //implementation("me.mattstudios.utils:matt-framework:1.4.6")
    //implementation("dev.triumphteam:triumph-gui:3.0.3")
}

tasks {
    withType<JavaCompile> {
        sourceCompatibility = targetJavaVersion
        targetCompatibility = targetJavaVersion
        options.encoding = "UTF-8"
    }
}