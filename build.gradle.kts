plugins {
    kotlin("jvm") version "1.6.0"

    id("com.github.johnrengelman.shadow") version "7.0.0"
    java
}

group = "me.corecraft"
version = "1.4-STABLE"

repositories {
    mavenCentral()

    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://oss.sonatype.org/content/repositories/central")
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://repo.mattstudios.me/artifactory/public/")
    maven("https://repo.codemc.org/repository/maven-public")
    maven("https://repo.essentialsx.net/releases/")
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.18.1-R0.1-SNAPSHOT")
    compileOnly("net.essentialsx:EssentialsX:2.19.0")

    compileOnly("org.jetbrains.kotlin:kotlin-stdlib:1.6.0")

    implementation("me.mattstudios.utils:matt-framework:1.4.6")

    implementation("dev.triumphteam:triumph-gui:3.0.5")
    implementation("me.mattstudios:triumph-msg-adventure:2.2.4-SNAPSHOT")
    implementation("org.bstats:bstats-bukkit:2.2.1")
    implementation("io.papermc:paperlib:1.0.7")
}

tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "17"
        }
    }

    shadowJar {
        archiveFileName.set("${rootProject.name}-${version}.jar")

        relocate("me.mattstudios.utils", "me.corecraft.libs")
        relocate("dev.triumphteam.gui", "me.corecraft.libs")
        relocate("org.bstats", "me.corecraft.libs")
        relocate("io.papermc.lib", "me.corecraft.libs")
    }
}