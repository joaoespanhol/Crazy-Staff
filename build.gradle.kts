plugins {
    kotlin("jvm") version "1.6.0"

    id("com.github.johnrengelman.shadow") version "7.0.0"
}

rootProject.group = "com.badbones"
rootProject.version = "v1.0.2"

repositories {
    mavenCentral()

    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://repo.mattstudios.me/artifactory/public/")
    maven("https://repo.codemc.org/repository/maven-public")
    maven("https://repo.essentialsx.net/releases/")
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.18.1-R0.1-SNAPSHOT")

    compileOnly("org.jetbrains.kotlin:kotlin-stdlib:1.6.0")

    //compileOnly("net.essentialsx:EssentialsX:2.19.0") {
    //    exclude("org.spigotmc")
    //}

    //implementation("me.mattstudios.utils:matt-framework:1.4.6")

    //implementation("dev.triumphteam:triumph-gui:3.0.5")
    //implementation("me.mattstudios:triumph-msg-adventure:2.2.4-SNAPSHOT")
    //implementation("org.bstats:bstats-bukkit:2.2.1")
    //implementation("io.papermc:paperlib:1.0.7")
}

tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "17"
        }
    }

    shadowJar {
        archiveFileName.set("${rootProject.name}-${rootProject.version}.jar")

        //relocate("me.mattstudios.utils", "com.badbones.libs")
        //relocate("dev.triumphteam.gui", "com.badbones.libs")
        //relocate("org.bstats", "com.badbones.libs")
        //relocate("io.papermc.lib", "com.badbones.libs")
    }
}