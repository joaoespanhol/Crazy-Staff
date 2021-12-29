package net.savagelabs.func.types.json

import com.google.gson.GsonBuilder
import net.savagelabs.func.types.json.adapters.InventoryTypeAdapter
import net.savagelabs.func.types.json.adapters.LocalTimeTypeAdapter
import net.savagelabs.func.types.json.adapters.LocationTypeAdapter
import net.savagelabs.types.json.adapters.EnumTypeAdapter
import org.bukkit.Location
import org.bukkit.inventory.Inventory
import java.io.*
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.time.LocalTime

class Persist(private val dataFolder: File, private val data: Boolean) {

    private val json = GsonBuilder()
        .setPrettyPrinting()
        .disableHtmlEscaping()
        .enableComplexMapKeySerialization()
        .excludeFieldsWithModifiers(128, 64)
        .registerTypeAdapter(LocalTime::class.java, LocalTimeTypeAdapter())
        .registerTypeAdapter(Inventory::class.java, InventoryTypeAdapter())
        .registerTypeAdapter(Location::class.java, LocationTypeAdapter())
        .registerTypeAdapterFactory(EnumTypeAdapter.ENUM_FACTORY).create()

    private val dataJson = GsonBuilder()
        .disableHtmlEscaping()
        .enableComplexMapKeySerialization()
        .excludeFieldsWithModifiers(128, 64)
        .registerTypeAdapter(LocalTime::class.java, LocalTimeTypeAdapter())
        .registerTypeAdapter(Inventory::class.java, InventoryTypeAdapter())
        .registerTypeAdapter(Location::class.java, LocationTypeAdapter())
        .registerTypeAdapterFactory(EnumTypeAdapter.ENUM_FACTORY).create()

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
                saveFile(default!!, file)
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

    private fun saveFile(instance: Any, file: File) {

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
        return DiscUtil(dataFolder).write(DiscUtil.DefaultFile(data, file, json.toJson(instance)))
    }
}