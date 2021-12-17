package net.savagelabs.func.persist

import kotlinx.serialization.Serializable
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets

object Config {

    var testString = "Hello"

}

data class DefaultFile(val dataEnabled: Boolean, val file: File, val contents: String)

fun load() {

}

fun write(plugin: JavaPlugin, parent: DefaultFile) {
    val content = parent.contents
    val file = parent.file

    val placeholder = File(plugin.dataFolder, file.name)

    runCatching {
        placeholder.createNewFile()
        val stream = FileOutputStream(placeholder)
        OutputStreamWriter(stream, StandardCharsets.UTF_8).use { it.write(content) }
    }
}

@Serializable
data class Project(val name: String)