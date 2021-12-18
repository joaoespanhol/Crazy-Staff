import dev.triumphteam.helper.core
import dev.triumphteam.helper.PlatformType
import dev.triumphteam.helper.feature
import dev.triumphteam.helper.Feature

plugins {
    kotlin("jvm") version "1.6.0"
    kotlin("plugin.serialization") version "1.5.31"

    id("me.mattstudios.triumph") version "0.2.6"

    id("com.github.johnrengelman.shadow") version "7.0.0"
    java
}

group = "net.savagelabs"
version = "1.0-STABLE"

val targetJavaVersion = "17"

repositories {
    mavenCentral()

    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://repo.mattstudios.me/artifactory/public/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.18-R0.1-SNAPSHOT")

    compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.1")
    compileOnly("org.jetbrains.kotlin:kotlin-stdlib:1.6.0")

    //api("net.kyori:adventure-api:4.9.3")

    //implementation("me.mattstudios.utils:matt-framework:1.4.6")
    //implementation("dev.triumphteam:triumph-gui:3.0.3")

    //implementation("me.mattstudios:triumph-config:1.0.5-SNAPSHOT")

    implementation(core(PlatformType.BUKKIT, "2.0.1-SNAPSHOT"))
    implementation(feature(Feature.CONFIG, "2.0.1-SNAPSHOT"))
}

tasks {
    withType<JavaCompile> {
        sourceCompatibility = targetJavaVersion
        targetCompatibility = targetJavaVersion
        options.encoding = "UTF-8"
    }

    shadowJar {
        archiveFileName.set("${rootProject.name}-${version}.jar")
    }
}