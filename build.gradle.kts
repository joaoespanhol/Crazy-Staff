plugins {
    kotlin("jvm") version "1.6.0"
}

rootProject.group = "com.badbones69"
rootProject.version = "1.0.2"

repositories {
    mavenCentral()

    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.18.1-R0.1-SNAPSHOT")

    compileOnly(kotlin("stdlib", "1.6.0"))
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}