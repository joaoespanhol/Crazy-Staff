package net.savagelabs.func.json

import kotlinx.serialization.json.Json
import org.bukkit.plugin.java.JavaPlugin

class Serializer(private val plugin: JavaPlugin) {
    private val json = Json {
        this.prettyPrint = false
    }
}