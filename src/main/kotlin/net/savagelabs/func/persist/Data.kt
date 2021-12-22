package net.savagelabs.func.persist

import net.savagelabs.commands.getSavedPlayer
import net.savagelabs.commands.hideStaff
import net.savagelabs.func.types.json.JsonManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

object Data {

    var nextPlayerID: Int = 0
    var players = hashMapOf<UUID, Player>()

    fun load(plugin: JavaPlugin) = JsonManager(plugin, true).loadFromFile(this, Data::class.java, "data.json")

    fun save(plugin: JavaPlugin) = JsonManager(plugin, true).saveToFile(this, "data.json")
}

data class Player(val uuid: UUID) {

    var isFrozen: Boolean = false

    var isStaff: Boolean = false
    var isVanished: Boolean = false

    var armorContents = arrayOf<ItemStack>()
    var inventoryContents = arrayOf<ItemStack>()
}

class DataListener(private val plugin: JavaPlugin) : Listener {

    @EventHandler
    fun onPlayerJoin(e: PlayerJoinEvent): Unit = with(e) {

        when {
            player.getSavedPlayer() != null -> {
                if (player.getSavedPlayer()!!.isStaff) player.hideStaff(plugin)
            }
            else -> {
                createPlayer(player)
                Data.save(plugin)
            }
        }
    }

    private fun createPlayer(player: org.bukkit.entity.Player): Player {
        val newPlayer = Player(player.uniqueId)
        Data.players[player.uniqueId] = newPlayer
        Data.nextPlayerID++
        return newPlayer
    }
}