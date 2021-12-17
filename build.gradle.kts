plugins {
    id("com.github.johnrengelman.shadow") version "7.0.0"
    kotlin("jvm") version "1.6.0"
}

group = "net.savagelabs"
description = "A simple staff plugin"
version = "1.0-STABLE"

repositories {
    mavenCentral()

    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://repo.mattstudios.me/artifactory/public/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.18-R0.1-SNAPSHOT")

    compileOnly("org.jetbrains.kotlin:kotlin-stdlib:1.6.0")

    implementation("me.mattstudios.utils:matt-framework:1.4.6")
    implementation("dev.triumphteam:triumph-gui:3.0.3")
}

tasks {
    shadowJar {
        archiveFileName.set("StaffX-[v$version].jar")
    }
}