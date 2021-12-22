package net.savagelabs.func.types.yaml

import java.io.File
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.FileOutputStream
import org.bukkit.plugin.Plugin
import java.io.InputStream
import java.util.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class FileManager {

    //TODO convert this into a system like JsonManager ( remove the Enum but tis optional )

    private val files: MutableMap<Files, File> = EnumMap(Files::class.java)
    private val configurations: MutableMap<Files, FileConfiguration> = EnumMap(Files::class.java)

    fun setup(plugin: Plugin) {
        if (!plugin.dataFolder.exists()) plugin.dataFolder.mkdirs()
        files.clear()
        configurations.clear()

        Files.values().forEach {
            val newFile = File(plugin.dataFolder, it.fileName)
            if (!newFile.exists()) {
                runCatching {
                    val serverFile = File(plugin.dataFolder, "/${it.fileName}")
                    val jarFile = javaClass.getResourceAsStream("/${it.fileName}")
                    copyFile(jarFile, serverFile)
                }.onFailure { e -> plugin.logger.info(e.message) }
                files[it] = newFile
                configurations[it] = YamlConfiguration.loadConfiguration(newFile)
            }
        }
    }

    fun getFile(file: Files): FileConfiguration? = configurations[file]

    fun saveFile(file: Files) = runCatching { configurations[file]!!.save(files[file]!!)}

    fun reloadFile(file: Files) {
        configurations[file] = YamlConfiguration.loadConfiguration(files[file]!!)
    }

    fun reloadAllFiles() = Files.values().forEach { it.reloadFile() }

    private fun copyFile(inStream: InputStream, out: File) {
        inStream.use { fis ->
            FileOutputStream(out).use { fos ->
                val buf = ByteArray(1024)
                var i: Int
                while (fis.read(buf).also { i = it } != -1) {
                    fos.write(buf, 0, i)
                }
            }
        }
    }

    enum class Files(val fileName: String) {
        PLACEHOLDER("placeholder.yml");

        val file: FileConfiguration? get() = FileManager().getFile(this)

        fun saveFile() = FileManager().saveFile(this)

        fun reloadFile() = FileManager().reloadFile(this)
    }
}