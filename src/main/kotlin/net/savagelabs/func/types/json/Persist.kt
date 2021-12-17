package net.savagelabs.func.types.json

import com.google.gson.GsonBuilder
import java.io.*
import java.nio.charset.StandardCharsets
import java.nio.file.Files

class Persist(private val dataFolder: File, private val data: Boolean) {

    private val json = GsonBuilder()
        .setPrettyPrinting()
        .disableHtmlEscaping()
        .enableComplexMapKeySerialization()
        .excludeFieldsWithModifiers(128, 64).create()

    private val dataJson = GsonBuilder()
        .disableHtmlEscaping()
        .enableComplexMapKeySerialization()
        .excludeFieldsWithModifiers(128, 64).create()

    private fun getFile(data: Boolean, name: String): File {
        var dataFolder = File("./")

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

   fun <T> load(default: T, classObject: Class<T>, file: File): T {
        when {
            !file.exists() -> {
                save(default!!, getFile(data, file.name))
                return default
            }
            else -> {
                val loaded = loadClass(classObject, getFile(data, file.name))
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

    fun save(instance: Any, file: File) {

        val json = when (data) {
            true -> this.dataJson
            false -> this.json
        }

        val broken = File("${file.toPath()}.back-up")
        if (file.exists()) {
            runCatching {
                if (broken.exists()) broken.delete()
                Files.copy(file.toPath(), broken.toPath())
            }.onFailure { println(it.message) }
        }

        broken.delete()
        return DiscUtil(dataFolder).write(DiscUtil.DefaultFile(data, file, json.toJson(instance)))
    }
}