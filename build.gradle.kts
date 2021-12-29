plugins {
    kotlin("jvm") version "1.6.0"

    id("com.github.johnrengelman.shadow") version "7.0.0"
    java
}

group = "net.savagelabs"
version = "1.0-STABLE"

repositories {
    mavenCentral()

    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://oss.sonatype.org/content/repositories/central")
    maven("https://repo.mattstudios.me/artifactory/public/")

    maven("https://repo.codemc.org/repository/maven-public")
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.17.1-R0.1-SNAPSHOT")

    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.6.0")

    implementation("me.mattstudios.utils:matt-framework:1.4.6")
    implementation("dev.triumphteam:triumph-gui:3.0.5")
    implementation("me.mattstudios:triumph-msg-adventure:2.2.4-SNAPSHOT")
}

tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "17"
        }
    }

    shadowJar {
        archiveFileName.set("${rootProject.name}-${version}.jar")

        relocate("me.mattstudios.utils", "net.savagelabs.libs")
        relocate("dev.triumphteam.gui", "net.savagelabs.libs")
    }
}