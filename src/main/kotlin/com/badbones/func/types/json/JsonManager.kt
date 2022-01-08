package com.badbones.func.types.json

import com.badbones.func.types.json.adapters.InventoryTypeAdapter
import com.badbones.func.types.json.adapters.LocalTimeTypeAdapter
import com.badbones.func.types.json.adapters.LocationTypeAdapter
import com.google.gson.GsonBuilder
import org.bukkit.Location
import org.bukkit.inventory.Inventory
import java.io.*
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.time.LocalTime

class JsonManager(private val dataFolder: File, private val data: Boolean) {

    private val json = GsonBuilder()
        .setPrettyPrinting()
        .disableHtmlEscaping()
        .enableComplexMapKeySerialization()
        .excludeFieldsWithModifiers(128, 64)
        .registerTypeAdapter(LocalTime::class.java, LocalTimeTypeAdapter())
        .registerTypeAdapter(Inventory::class.java, InventoryTypeAdapter())
        .registerTypeAdapter(Location::class.java, LocationTypeAdapter())
        .registerTypeAdapterFactory(com.badbones.types.json.adapters.EnumTypeAdapter.ENUM_FACTORY).create()

    private val dataJson = GsonBuilder()
        .disableHtmlEscaping()
        .enableComplexMapKeySerialization()
        .excludeFieldsWithModifiers(128, 64)
        .registerTypeAdapter(LocalTime::class.java, LocalTimeTypeAdapter())
        .registerTypeAdapter(Inventory::class.java, InventoryTypeAdapter())
        .registerTypeAdapter(Location::class.java, LocationTypeAdapter())
        .registerTypeAdapterFactory(com.badbones.types.json.adapters.EnumTypeAdapter.ENUM_FACTORY).create()

    private fun getFile(data: Boolean, name: String): File {
        var dataFolder = this.dataFolder

        if (data) dataFolder = File(dataFolder, "/data")

        return File(dataFolder, name)
    }

    private fun <T> loadClass(classObject: Class<T>, file: File): T? {
        if (file.exists()) {
            runCatching {
                val stream = FileInputStream(file)
                val reader = InputStreamReader(stream, StandardCharsets.UTF_8)
                return json.fromJson(reader, classObject)
            }
        }
        return null
    }

    fun <T> load(default: T, classObject: Class<T>, name: String) = loadFile(default, classObject, getFile(data, name))

    fun save(instance: Any, name: String) = saveFile(instance, getFile(data, name))

    private fun <T> loadFile(default: T, classObject: Class<T>, file: File): T {
        when {
            !file.exists() -> {
                saveFile(default, file)
                return default
            }
            else -> {
                val loaded = loadClass(classObject, file)
                if (loaded == null) {
                    val backup = File("${file.path}_bad")

                    if (backup.exists()) backup.delete()
                    file.renameTo(backup)
                    return default
                }
                return loaded
            }
        }
    }

    private fun saveFile(instance: Any?, file: File) {

        val json = when (data) {
            true -> this.dataJson
            false -> this.json
        }

        val broken = File("${file.toPath()}.back-up")
        if (file.exists()) {
            runCatching {
                if (broken.exists()) broken.delete()
                Files.copy(file.toPath(), broken.toPath())
            }
        }

        broken.delete()
        return write(DefaultFile(data, file, json.toJson(instance)))
    }

    data class DefaultFile(val dataEnabled: Boolean, val file: File, val contents: String)

    private fun write(parent: DefaultFile) {
        val content = parent.contents
        val file = parent.file

        val fileObject = when (parent.dataEnabled) {
            true -> {
                val dataPath = File("$dataFolder/data")
                if (!dataPath.exists()) dataPath.mkdirs()
                File(dataPath, file.name)
            }
            false -> {
                File(dataFolder, file.name)
            }
        }

        runCatching {
            fileObject.createNewFile()
            val stream = FileOutputStream(fileObject)
            OutputStreamWriter(stream, StandardCharsets.UTF_8).use { it.write(content) }
        }
    }
}

class Serializer(private val dataFolder: File, private val data: Boolean) {
    fun <T> load(default: T, classObject: Class<T>, name: String) = JsonManager(dataFolder, data).load(default, classObject, name)

    fun save(instance: Any, name: String) = JsonManager(dataFolder, data).save(instance, name)
}