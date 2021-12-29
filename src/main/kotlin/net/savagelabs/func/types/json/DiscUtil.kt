package net.savagelabs.func.types.json

import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets

class DiscUtil(private val dataFolder: File) {

    data class DefaultFile(val dataEnabled: Boolean, val file: File, val contents: String)

    fun write(parent: DefaultFile) {
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