package me.corecraft.func.types.json.adapters

import com.google.gson.*
import org.bukkit.Bukkit
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.util.io.BukkitObjectInputStream
import org.bukkit.util.io.BukkitObjectOutputStream
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.lang.reflect.Type

class InventoryTypeAdapter : JsonSerializer<Inventory>, JsonDeserializer<Inventory?> {

    override fun serialize(inventory: Inventory, type: Type, jsonContext: JsonSerializationContext): JsonElement {
        val jsonObject = JsonObject()
        jsonObject.add("contents", JsonPrimitive(toBase64(inventory)))
        return jsonObject
    }

    override fun deserialize(jsonElement: JsonElement, type: Type, jsonContext: JsonDeserializationContext): Inventory? {
        val jsonObject = jsonElement.asJsonObject
        return fromBase64(jsonObject["contents"].asString)
    }

    companion object {
        fun toBase64(inventory: Inventory): String {
            return runCatching {
                val outputStream = ByteArrayOutputStream()
                val dataOutput = BukkitObjectOutputStream(outputStream)

                // Write the size of the inventory
                dataOutput.writeInt(inventory.size)

                // Save every element in the list
                for (i in 0 until inventory.size) {
                    dataOutput.writeObject(inventory.getItem(i))
                }
                dataOutput.close()
                Base64Coder.encodeLines(outputStream.toByteArray())
            }.onFailure { it.printStackTrace() }.toString()
        }

        fun fromBase64(data: String?): Inventory? {
            runCatching {
                val inputStream = ByteArrayInputStream(Base64Coder.decodeLines(data))
                val dataInput = BukkitObjectInputStream(inputStream)
                val inventory = Bukkit.getServer().createInventory(null, dataInput.readInt())

                for (i in 0..inventory.size) inventory.setItem(i, dataInput.readObject() as ItemStack)

                dataInput.close()
                return inventory
            }.onFailure { it.printStackTrace() }
            return null
        }
    }
}