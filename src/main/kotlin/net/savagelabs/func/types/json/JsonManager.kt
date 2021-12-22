package net.savagelabs.func.types.json

import com.google.gson.GsonBuilder
import net.savagelabs.types.json.adapters.EnumTypeAdapter
import net.savagelabs.func.types.json.adapters.LocalTimeTypeAdapter
import org.bukkit.plugin.java.JavaPlugin
import java.io.*
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.time.LocalTime

class JsonManager(private val plugin: JavaPlugin, private val data: Boolean) {

    private val json = GsonBuilder()
        .setPrettyPrinting()
        .disableHtmlEscaping()
        .enableComplexMapKeySerialization()
        .excludeFieldsWithModifiers(128, 64)
        .registerTypeAdapter(LocalTime::class.java, LocalTimeTypeAdapter())
        .registerTypeAdapterFactory(EnumTypeAdapter.ENUM_FACTORY).create()

    private val dataJson = GsonBuilder()
        .disableHtmlEscaping()
        .enableComplexMapKeySerialization()
        .excludeFieldsWithModifiers(128, 64)
        .registerTypeAdapter(LocalTime::class.java, LocalTimeTypeAdapter())
        .registerTypeAdapterFactory(EnumTypeAdapter.ENUM_FACTORY).create()

    // Call this once on reloads / shutdown
    fun saveToFile(instance: Any, name: String) {
        val file = File(name)

        val json = when (data) {
            true -> this.dataJson
            false -> this.json
        }

        val broken = File("${file.toPath()}.back-up")
        if (file.exists()) {
            runCatching {
                if (broken.exists()) broken.delete()
                Files.copy(file.toPath(), broken.toPath())
            }.onFailure { plugin.logger.info(it.message) }
        }

        broken.delete()
        return write(DefaultFile(file, json.toJson(instance)))
    }

    // Call this only on loads.
    fun <T> loadFromFile(default: T, classObject: Class<T>, name: String): T? {
        val file = File(name)

        fun <T> loadClass(classObject: Class<T>): T? {
            if (file.exists()) {
                runCatching {
                    val stream = FileInputStream(file)
                    val reader = InputStreamReader(stream, StandardCharsets.UTF_8)
                    return json.fromJson(reader, classObject)
                }
            }
            return null
        }

        fun createBackUp(): T {
            val backup = File("${file.path}_bad")
            if (backup.exists()) backup.delete()
            file.renameTo(backup)
            return default
        }

        when {
            !file.exists() -> {
                saveToFile(default!!, name)
                return default
            }
            else -> {
                val loaded = loadClass(classObject)
                if (loaded == null) {
                    createBackUp()
                    return default
                }
                return loaded
            }
        }
    }

    // This will write to the file
    private fun write(parent: DefaultFile) {
        val content = parent.contents
        val file = parent.file

        val fileObject = when (data) {
            true -> {
                val dataPath = File("${plugin.dataFolder}/data")
                if (!dataPath.exists()) dataPath.mkdirs()
                File(dataPath, file.name)
            }
            false -> {
                File(plugin.dataFolder, file.name)
            }
        }

        runCatching {
            fileObject.createNewFile()
            val stream = FileOutputStream(fileObject)
            OutputStreamWriter(stream, StandardCharsets.UTF_8).use { it.write(content) }
        }.onFailure { plugin.logger.info(it.message) }
    }

    data class DefaultFile(val file: File, val contents: String)
}