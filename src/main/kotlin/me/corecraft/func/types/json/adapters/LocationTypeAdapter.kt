package me.corecraft.func.types.json.adapters

import com.google.gson.*
import org.bukkit.Bukkit
import org.bukkit.Location
import java.lang.reflect.Type

class LocationTypeAdapter() : JsonSerializer<Location>, JsonDeserializer<Location?> {
    override fun serialize(location: Location, type: Type, jsonContext: JsonSerializationContext): JsonElement {
        val jsonObject = JsonObject()
        runCatching {
            jsonObject.add("x", JsonPrimitive(location.x))
            jsonObject.add("y", JsonPrimitive(location.y))
            jsonObject.add("z", JsonPrimitive(location.z))
            jsonObject.add("world", JsonPrimitive(location.world.name))
        }.onFailure { it.printStackTrace() }
        return jsonObject
    }

    override fun deserialize(jsonElement: JsonElement, type: Type, jsonContext: JsonDeserializationContext): Location? {
        val jsonObject = jsonElement.asJsonObject
        runCatching {
            Bukkit.getServer().getWorld(jsonObject["world"].asString)
            jsonObject["x"].asDouble
            jsonObject["y"].asDouble
            jsonObject["z"].asDouble
        }.onFailure { it.printStackTrace() }
        return null
    }
}