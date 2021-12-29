package me.corecraft.func.types.json

import java.io.File

class Serializer(private val dataFolder: File, private val data: Boolean) {
    fun <T> load(default: T, classObject: Class<T>, name: String) = Persist(dataFolder, data).load(default, classObject, name)

    fun save(instance: Any, name: String) = Persist(dataFolder, data).save(instance, name)
}